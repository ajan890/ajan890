import {tiny} from "../tiny-graphics.js";

const {vec3} = tiny;
const DEBUG_MODE = false;

// Math framework for splines
export class Spline {
    constructor() {
        this.points = [];
        this.tangents = [];
        this.size = 0;
        this.divisions = [];
    }

    add_point(x, y, z, sx, sy, sz) {
        if (DEBUG_MODE) console.log("add point function called");
        this.points.push(vec3(x, y, z));
        this.tangents.push(vec3(sx, sy, sz));
        this.size += 1;
    }

    set_tangent(index, x, y, z) {
        if (DEBUG_MODE) console.log("set tangent function called");
        if (index >= this.points.size || index < 0) {
            console.log("Invalid index: set_tangent(" + index + ", " + x + ", " + y + ", " + z + ")");
            return;
        }
        this.tangents[index] = vec3(x, y, z);
    }

    set_point(index, x, y, z) {
        if (DEBUG_MODE) console.log("set point function called");
        if (index >= this.points.size || index < 0) {
            console.log("Invalid index: set_point(" + index + ", " + x + ", " + y + ", " + z + ")");
            return;
        }
        this.points[index] = vec3(x, y, z);
    }

    insert_point(index, x, y, z, sx, sy, sz) {
        if (DEBUG_MODE) console.log("insert point function called");
        if (index >= this.points.size || index < 0) {
            console.log("Invalid index: insert_point(" + index + ", " + x + ", " + y + ", " + z + ", " + sx + ", " + sy + ", " + sz + ")");
            return;
        }
        this.points.splice(index, 0, new vec3(x, y, z));
        this.tangents.splice(index, 0, new vec3(sx, sy, sz));
        this.size++;
    }

    delete_point(index) {
        if (DEBUG_MODE) console.log("delete point function called");
        if (index >= this.points.size || index < 0) {
            console.log("Invalid index: delete_point(" + index + ")");
            return;
        }
        this.points.splice(index, 1);
        this.tangents.splice(index, 1);
        this.size--;
    }

    clear_spline() {
        this.points = [];
        this.tangents = [];
        this.size = 0;
        this.divisions = [];
    }

    toString() {
        let text = "Spline Object - " + this.size + " points:\n";
        for (let i = 0; i < this.size; i++) {
            text += "Point " + i + ": (" + this.points[i].toString() + "), [" + this.tangents[i].toString() + "]\n";
        }
        return text;
    }

    // returns an arr[4] of coefficients, At^3 + Bt^2 + Ct + D, in that order.  A, B, C, D are vec3.
    _calculate_eq_coef(P_0, v_0, P_1, v_1) {
        let A = P_0.times(2).plus(v_0).plus(P_1.times(-2)).plus(v_1);
        let B = P_0.times(-3).plus(v_0.times(-2)).plus(P_1.times(3)).minus(v_1);
        let C = v_0;
        let D = P_0;

        if (DEBUG_MODE) console.log("A" + A + "\nB: " + B + "\nC: " + C + "\nD: " + D);
        return [A, B, C, D]
    }

    set_subdivisions(num_divisions) {
        if (this.size < 2 || num_divisions < 2) return this.points

        let points = [];
        let t_increment = 1.0 / num_divisions;
        for (let i = 0; i < this.size - 1; i++) {
            let coef = this._calculate_eq_coef(this.points[i], this.tangents[i], this.points[i + 1], this.tangents[i + 1]);
            for (let j = 0; j < num_divisions; j++) {
                let t = (j * t_increment);
                if (DEBUG_MODE) console.log(t);
                points.push(coef[0].times(t * t * t).plus(coef[1].times(t * t)).plus(coef[2].times(t)).plus(coef[3]));
            }
        }
        points.push(this.points[this.size - 1]);
        this.divisions = points;
    }

    get_arc_length() {
        if (this.size < 2) return 0

        let curr = this.points[0];
        let prev = vec3(0, 0, 0);
        let total_length = 0.0;
        let t_increment = 0.001;
        for (let i = 0; i < this.size - 1; i++) {
            let coef = this._calculate_eq_coef(this.points[i], this.tangents[i], this.points[i + 1], this.tangents[i + 1]);
            for (let j = 0; j < 1000; j++) {
                let t = (j * t_increment);
                if (DEBUG_MODE) console.log(t);
                prev = curr;
                curr = coef[0].times(t * t * t).plus(coef[1].times(t * t)).plus(coef[2].times(t)).plus(coef[3]);
                total_length += Math.sqrt((curr[0] - prev[0]) ** 2 + (curr[1] - prev[1]) ** 2 + (curr[2] - prev[2]) ** 2);
            }
        }
        let final = this.points[this.size - 1];
        total_length += Math.sqrt((curr[0] - final[0]) ** 2 + (curr[1] - final[1]) ** 2 + (curr[2] - final[2]) ** 2)
        return total_length;
    }
}