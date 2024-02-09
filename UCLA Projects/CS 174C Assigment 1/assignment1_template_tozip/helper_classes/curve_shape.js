import {defs, tiny} from "../examples/common.js";
const {vec3, color, Mat4, Shape} = tiny;

const DEBUG_MODE = false;
export class Curve_Shape extends Shape {
    constructor() {
        super("position", "normal");
        this.material = { shader: new defs.Phong_Shader(), ambient: 1.0, color: color(1, 0, 0, 1)}
        this.arrays.position = [];
        this.arrays.normal = [];
    }

    set_points(points) {
        this.arrays.position = points;
        this.arrays.normal = new Array(this.arrays.position.length).fill(vec3(0, 0, 0));
    }

    draw(webgl_manager, uniforms) {
        if (DEBUG_MODE) {
            console.log(this.arrays.position);
            console.log(this.arrays.normal);
        }

        if (this.arrays.position.length > 0)
            super.draw(webgl_manager, uniforms, Mat4.identity(), this.material, "LINE_STRIP");
    }
}