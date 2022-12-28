#include <omp.h>
#include "kernel.h"
#define TILE_SIZE 32
void kernel_omp(int *input, int *ref, int64_t rows, int64_t cols, int penalty) {
 int64_t tileCols = (cols - 1) / TILE_SIZE;
 int64_t i, j;
 //intermediate calculation constants to reduce need for multiplication
 int64_t temp0 = cols;
 int64_t temp1 = cols * rows;
 const int64_t tempInc1 = TILE_SIZE * (cols - 1);
 const int64_t tempInc2 = tempInc1 + TILE_SIZE;

 for (i = 0; i < tileCols; i++) {
	 #pragma omp parallel for
	 for (j = 0; j <= i; j++) {											//can't move tempInc * j outside for
		 int64_t idx = temp0 + j * tempInc1;							// 0 1 2 3 4 5
		 int64_t idxNW = idx - cols - 1;								// 1 X X X X X
		 int64_t idxN = idx - cols;										// 2 X X X X _
		 int64_t idxW = idx - 1;										// 3 X X X _ _
		 for (int k = 0; k < TILE_SIZE; k++) {							// 4 X X _ _ _
			 for (int l = 0; l < TILE_SIZE; l++) {						// 5 X _ _ _ _
				 idx++;		//memory dump position of input[i][j]
				 idxNW++;	//memory dump position of input[i-1][j-1]
				 idxN++;	//memory dump position of input[i-1][j]
				 idxW++;	//memory dump position of input[i][j-1]
				 int r = ref[idx];
				 int inputNW = input[idxNW];	//value at idxNW
				 int inputW = input[idxW];		//value at idxW
				 int inputN = input[idxN];		//value at idxN
				 input[idx] = maximum(inputNW + r, inputW - penalty, inputN - penalty);
			 }
			 idx += (cols - TILE_SIZE);
			 idxNW += (cols - TILE_SIZE);
			 idxN += (cols - TILE_SIZE);
			 idxW += (cols - TILE_SIZE);
		 }
	 }
	 temp0 += TILE_SIZE;
 }

 for (i = 1; i < tileCols; i++) {
	 #pragma omp parallel for
	 for (j = i; j < tileCols; j++) {
		 int64_t idx = temp1 - j * tempInc1;							// 0 1 2 3 4 5
		 int64_t idxNW = idx - cols - 1;								// 1 _ _ _ _ _
		 int64_t idxN = idx - cols;										// 2 _ _ _ _ X
		 int64_t idxW = idx - 1;										// 3 _ _ _ X X
		 for (int k = 0; k < TILE_SIZE; k++) {							// 4 _ _ X X X
			 for (int l = 0; l < TILE_SIZE; l++) {						// 5 _ X X X X
				 idx++;		//memory dump position of input[i][j]
				 idxNW++;	//memory dump position of input[i-1][j-1]
				 idxN++;	//memory dump position of input[i-1][j]
				 idxW++;	//memory dump position of input[i][j-1]
				 int r = ref[idx];
				 int inputNW = input[idxNW];	//value at idxNW
				 int inputW = input[idxW];		//value at idxW
				 int inputN = input[idxN];		//value at idxN
				 input[idx] = maximum(inputNW + r, inputW - penalty, inputN - penalty);
			 }
			 idx += (cols - TILE_SIZE);
			 idxNW += (cols - TILE_SIZE);
			 idxN += (cols - TILE_SIZE);
			 idxW += (cols - TILE_SIZE);
		 }
	 }
	 temp1 += tempInc2;
 }
}