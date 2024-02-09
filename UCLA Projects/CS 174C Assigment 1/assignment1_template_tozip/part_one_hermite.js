import {tiny, defs} from './examples/common.js';

// Import helper classes
import {Spline} from './helper_classes/spline.js';
import {Curve_Shape} from "./helper_classes/curve_shape.js";

// Pull these names into this module's scope for convenience:
const {vec3, vec4, color, Mat4, Shape, Material, Shader, Texture, Component} = tiny;

const DEBUG_MODE = true;

// Controls how many subdivisions the space between each control point is divided into.
// Higher Number = Higher Resolution.
const RENDERING_SUBDIVISIONS = 100;

export const Part_one_hermite_base = defs.Part_one_hermite_base =
    class Part_one_hermite_base extends Component {
        init() {
            console.log("init")
            this.hover = this.swarm = false;
            this.shapes = {
                'box': new defs.Cube(),
                'ball': new defs.Subdivision_Sphere(4),
                'axis': new defs.Axis_Arrows(),
                'curve': new Curve_Shape()
            };
            const phong = new defs.Phong_Shader();
            const tex_phong = new defs.Textured_Phong();
            this.materials = {};
            this.materials.plastic = {
                shader: phong,
                ambient: .2,
                diffusivity: 1,
                specularity: .5,
                color: color(.9, .5, .9, 1)
            }
            this.materials.metal = {
                shader: phong,
                ambient: .2,
                diffusivity: 1,
                specularity: 1,
                color: color(.9, .5, .9, 1)
            }
            this.materials.rgb = {shader: tex_phong, ambient: .5, texture: new Texture("assets/rgb.jpg")}

            this.ball_location = vec3(1, 1, 1);
            this.ball_radius = 0.25;

            this.spline = new Spline();
        }

        render_animation(caller) {
            if (!caller.controls) {
                this.animated_children.push(caller.controls = new defs.Movement_Controls({uniforms: this.uniforms}));
                caller.controls.add_mouse_controls(caller.canvas);

                // !!! Camera changed here
                Shader.assign_camera(Mat4.look_at(vec3(10, 10, 10), vec3(0, 0, 0), vec3(0, 1, 0)), this.uniforms);
            }
            this.uniforms.projection_transform = Mat4.perspective(Math.PI / 4, caller.width / caller.height, 1, 100);

            // *** Lights: *** Values of vector or point lights.  They'll be consulted by
            // the shader when coloring shapes.  See Light's class definition for inputs.
            const t = this.t = this.uniforms.animation_time / 1000;
            const angle = Math.sin(t);

            // const light_position = Mat4.rotation( angle,   1,0,0 ).times( vec4( 0,-1,1,0 ) ); !!!
            // !!! Light changed here
            const light_position = vec4(20 * Math.cos(angle), 20, 20 * Math.sin(angle), 1.0);
            this.uniforms.lights = [defs.Phong_Shader.light_source(light_position, color(1, 1, 1, 1), 1000000)];

            // draw axis arrows.
            this.shapes.axis.draw(caller, this.uniforms, Mat4.identity(), this.materials.rgb);
        }
    }


export class Part_one_hermite extends Part_one_hermite_base {
    render_animation(caller) {
        super.render_animation(caller);

        /**********************************
         Start coding down here!!!!
         **********************************/
        const blue = color(0, 0, 1, 1), yellow = color(1, 0.7, 0, 1);
        const t = this.t = this.uniforms.animation_time / 1000;

        // !!! Draw ground
        let floor_transform = Mat4.translation(0, 0, 0).times(Mat4.scale(10, 0.01, 10));
        this.shapes.box.draw(caller, this.uniforms, floor_transform, {...this.materials.plastic, color: yellow});

        // !!! Draw ball (for reference)
        let ball_transform = Mat4.translation(this.ball_location[0], this.ball_location[1], this.ball_location[2])
            .times(Mat4.scale(this.ball_radius, this.ball_radius, this.ball_radius));
        this.shapes.ball.draw(caller, this.uniforms, ball_transform, {...this.materials.metal, color: blue});

        // TODO: you should draw spline here.
        this.shapes.curve.draw(caller, this.uniforms);
    }

    render_controls() {
        this.control_panel.innerHTML += "Part One:";
        this.new_line();
        this.key_triggered_button("Parse Commands", [], this.parse_commands);
        this.new_line();
        this.key_triggered_button("Draw", [], this.update_scene);
        this.new_line();
        this.key_triggered_button("Load", [], this.load_spline);
        this.new_line();
        this.key_triggered_button("Export", [], this.export_spline);
        this.new_line();
    }

    parse_commands() {
        let outputBoxValue = "parse_commands\n";
        let rawInput = document.getElementById("input").value;

        // parse commands into a list
        let commandList = rawInput.split('\n');
        commandList = commandList.map(str => str.trim()).filter(str => str.length !== 0);

        // run commands
        for (let i = 0; i < commandList.length; i++) {
            let divide = commandList[i].indexOf('<');
            let commandName, args
            if (divide !== -1) {
                commandName = commandList[i].substring(0, divide);
                args = commandList[i].substring(divide);

                // process args
                args = args.replace(/[<>,]/g, '').split(' ');
                args = args.map(str => parseInt(str)).filter(num => !isNaN(num));
            } else {
                // no args
                commandName = commandList[i];
                args = [];
            }

            // run commands
            switch (commandName.trim()) {
                case "add point":
                    if (DEBUG_MODE) console.log("add point");
                    if (args.length !== 6) console.log("add point: incorrect number of args, " + args.length + " given, 6 required.");
                    else this.spline.add_point(args[0], args[1], args[2], args[3], args[4], args[5]);
                    break;
                case "set point":
                    if (DEBUG_MODE) console.log("set point");
                    if (args.length !== 4) console.log("set point: incorrect number of args, " + args.length + " given, 4 required.");
                    else this.spline.set_point(args[0], args[1], args[2], args[3]);
                    break;
                case "set tangent":
                    if (DEBUG_MODE) console.log("set tangent");
                    if (args.length !== 4) console.log("set tangent: incorrect number of args, " + args.length + " given, 4 required.");
                    else this.spline.set_tangent(args[0], args[1], args[2], args[3]);
                    break;
                case "print":
                    if (DEBUG_MODE) console.log("print");
                    if (args.length !== 0) console.log("Args included in print command; ignoring args...");
                    outputBoxValue += this.spline.toString();
                    break;
                case "insert point":
                    if (DEBUG_MODE) console.log("insert point");
                    if (args.length !== 7) console.log("delete point: incorrect number of args, " + args.length + " given, 7 required.");
                    else this.spline.insert_point(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                    break;
                case "delete point":
                    if (DEBUG_MODE) console.log("delete point");
                    if (args.length !== 1) console.log("delete point: incorrect number of args, " + args.length + " given, 1 required.");
                    else this.spline.delete_point(args[0]);
                    break;
                case "clear spline":
                    if (DEBUG_MODE) console.log("clear spline");
                    if (args.length !== 0) console.log("Args included in print command; ignoring args...");
                    this.spline.clear_spline()
                    outputBoxValue += "Spline cleared.\n";
                    break;
                case "get_arc_length":
                    if (DEBUG_MODE) console.log("get arc length");
                    if (args.length !== 0) console.log("Args included in print command; ignoring args...");
                    outputBoxValue += "Spline length: " + this.spline.get_arc_length() + "\n";
                    break;
                default:
                    console.log("Unknown command: " + commandName + ", skipping...");
            }
        }
        if (DEBUG_MODE) console.log(this.spline.toString());
        document.getElementById("output").value = outputBoxValue;
    }

    update_scene() { // callback for Draw button
        document.getElementById("output").value = "update_scene";
        this.spline.set_subdivisions(RENDERING_SUBDIVISIONS);
        this.shapes.curve.set_points(this.spline.divisions);
    }

    load_spline() {
        this.spline.clear_spline();
        let outputBoxValue = "load_spline\n";
        let rawInput = document.getElementById("input").value;
        // parse points into a list
        let pointList = rawInput.split('\n');
        pointList = pointList.map(str => str.trim()).filter(str => str.length !== 0);
        pointList.shift()

        // process points
        for (let i = 0; i < pointList.length; i++) {
            let point = pointList[i].replace(/[<>,]/g, '').split(' ');
            if (point.length !== 6) {
                outputBoxValue += "Invalid syntax: " + pointList[i] + "\n";
                return;
            }
            this.spline.add_point(point[0], point[1], point[2], point[3], point[4], point[5]);
        }

        document.getElementById("output").value = outputBoxValue;
    }

    export_spline() {
        let outputBoxValue = "export_spline\n<n>\n";
        for (let i = 0; i < this.spline.size; i++) {
            let cur_point = this.spline.points[i];
            let cur_tan = this.spline.tangents[i];
            outputBoxValue += "<" + cur_point[0] + " " + cur_point[1] + " " + cur_point[2] + " " + cur_tan[0] + " " + cur_tan[1] + " " + cur_tan[2] + ">\n";
        }
        document.getElementById("output").value = outputBoxValue;
    }
}



