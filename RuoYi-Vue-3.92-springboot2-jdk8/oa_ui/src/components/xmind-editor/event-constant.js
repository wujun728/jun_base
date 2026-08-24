/**
 * 针对编辑器内使用的 Event 名称常量统一在此定义
 * 
 */

export const EXEC_COMMAND = "XMIND_EXEC_COMMAND";
export const PADDING_CHANGE = "XMIND_PADDING_CHANGE";
export const SET_DATA = "XMIND_SET_DATA";
export const START_TEXT_EDIT = "XMIND_START_TEXT_EDIT";
export const END_TEXT_EDIT = "XMIND_END_TEXT_EDIT";
export const TOGGLE_ZEN_MODE = "XMIND_TOGGLE_ZEN_MODE";
export const CREATE_ASSOCIATIVE_LINE = "CREATE_ASSOCIATIVE_LINE";
export const START_PAINTER = "START_PAINTER";

// 子组件 emit 给顶层编辑器实例的事件名称
export const EDITOR_EVENTS = {
  EXEC_COMMAND,
  PADDING_CHANGE,
  SET_DATA,
  START_TEXT_EDIT,
  END_TEXT_EDIT,
  TOGGLE_ZEN_MODE,
  CREATE_ASSOCIATIVE_LINE,
  START_PAINTER
};

export const NODE_ACTIVE = "node_active";
export const DATA_CHANGE = "data_change";
export const VIEW_DATA_CHANGE = "view_data_change";
export const BACK_FORWARD = "back_forward";
export const NODE_CONTEXTMENU = "node_contextmenu";
export const NODE_CLICK = "node_click";
export const DRAW_CLICK = "draw_click";
export const EXPAND_BTN_CLICK = "expand_btn_click";
export const SVG_MOUSEDOWN = "svg_mousedown";
export const MOUSEUP = "mouseup";
export const MODE_CHANGE = "mode_change";
export const NODE_TREE_RENDER_END = "node_tree_render_end";
export const RICH_TEXT_SELECTION_CHANGE = "rich_text_selection_change";
export const TRANSFORMING_DOM_TO_IMAGES = "transforming-dom-to-images";
export const GENERALIZATION_NODE_CONTEXTMENU = "generalization_node_contextmenu";
export const PAINTER_START = "painter_start";
export const PAINTER_END = "painter_end";
export const SCROLLBAR_CHANGE = "scrollbar_change";
export const SCALE = "scale";
export const TRANSLATE = "translate";
export const NODE_ATTACHMENTCLICK = "node_attachmentClick";
export const NODE_ATTACHMENTCONTEXTMENU = "node_attachmentContextmenu";
export const DEMONSTRATE_JUMP = "demonstrate_jump";
export const EXIT_DEMONSTRATE = "exit_demonstrate";
export const MINI_MAP_VIEW_BOX_POSITION_CHANGE = "mini_map_view_box_position_change";
export const SEARCH_INFO_CHANGE = "search_info_change";
export const SEARCH_MATCH_NODE_LIST_CHANGE = "search_match_node_list_change";

// 编辑器实例内部的所有事件名称
export const FORWARD_EDITOR_INNER_EVENTS = [
  NODE_ACTIVE,
  DATA_CHANGE,
  VIEW_DATA_CHANGE,
  BACK_FORWARD,
  NODE_CONTEXTMENU,
  NODE_CLICK,
  DRAW_CLICK,
  EXPAND_BTN_CLICK,
  SVG_MOUSEDOWN,
  MOUSEUP,
  MODE_CHANGE,
  NODE_TREE_RENDER_END,
  RICH_TEXT_SELECTION_CHANGE,
  TRANSFORMING_DOM_TO_IMAGES,
  GENERALIZATION_NODE_CONTEXTMENU,
  PAINTER_START,
  PAINTER_END,
  SCROLLBAR_CHANGE,
  SCALE,
  TRANSLATE,
  NODE_ATTACHMENTCLICK,
  NODE_ATTACHMENTCONTEXTMENU,
  DEMONSTRATE_JUMP,
  EXIT_DEMONSTRATE,
  MINI_MAP_VIEW_BOX_POSITION_CHANGE,
  SEARCH_INFO_CHANGE,
  SEARCH_MATCH_NODE_LIST_CHANGE
];

export const TOGGLE_MINI_MAP = "toggle_mini_map";
export const SHOW_SEARCH = "show_search";
export const CLOSE_SIDE_BAR = "closeSideBar";
export const SHOW_NODE_IMAGE = "showNodeImage";
export const SHOW_NODE_LINK = "showNodeLink";
export const SHOW_NODE_ICON = "showNodeIcon";
export const SHOW_NODE_TAG = "showNodeTag";
export const SHOW_NODE_NOTE = "showNodeNote";
export const CLOSE_NODE_ICON_TOOLBAR = "close_node_icon_toolbar";
export const SELECT_ATTACHMENT = "selectAttachment";