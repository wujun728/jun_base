import Viewer from "bpmn-js/lib/Viewer";
import ZoomScrollModule from "diagram-js/lib/navigation/zoomscroll";
import MoveCanvasModule from "diagram-js/lib/navigation/movecanvas";

export class CustomViewer extends Viewer {
  constructor(options) {
    super(options);
  }

  static get _modules() {
    return [
      ...Viewer.prototype._modules,
      ZoomScrollModule,
      MoveCanvasModule
    ];
  }
}