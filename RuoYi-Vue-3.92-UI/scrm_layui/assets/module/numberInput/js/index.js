/**
 * @name numberInput
 * @author HuangJunjie
 * @description layui 数值输入框扩展
 * @version 2.0.1.20210819
 */
layui.define(["jquery"], function (exports) {
    var $ = layui.jquery;
    var prefix = "number-input";

    /**
     * 链接外部样式文件
     * 由 码云 @hulangfy 同学建议
     */
    layui.link(layui.cache.base + 'numberInput/css/theme.css');

    /**
     * 创建样式列表
     * @param names
     * @returns {string}
     */
    function createClassList(flag, names) {
        if (typeof names == "string") names = [names];

        var classList = [];

        names.forEach(function (name) {
            classList.push([prefix, flag].join("-"));
            classList.push([name, flag].join("-"));
        });

        return classList.join(" ");
    }

    /**
     * 精度数字
     * @param value
     * @param precision
     */
    function ratioNumber(value, precision) {
        if (precision === undefined) precision = 0;
        var floatVal = parseFloat(value) || 0;
        var ratio = Math.pow(10, precision);

        return ((floatVal * ratio) / ratio).toFixed(precision);
    }

    /**
     * 检查是否包含特定属性
     * @param {*} $el
     * @param {*} attr
     * @returns
     */
    function hasAttr($el, attr) {
        var attrs = $el[0].attributes;

        for (var i = 0; i < attrs.length; i++) {
            if (attrs[i].name === attr) {
                return attrs[i].value !== "false";
            }
        }

        return false;
    }

    /**
     * 初始化操作
     * @param id
     * @param opts
     */
    function render(id, opts) {
        if (!opts) opts = {};

        // 绑定Dom
        $(id).each(function () {
            (function ($input) {
                var $target = $input.parent();
                // 取出配置
                var max = parseFloat(opts.max || $input.data("max")) || 999999999;
                var min = parseFloat(opts.min || $input.data("min")) || 0;
                var precision = parseInt(opts.precision || $input.data("precision")) || 0;
                var ratio = Math.pow(10, precision);
                var step = opts.step ? parseFloat(opts.step || $input.date("step")) : ((parseFloat(opts.step) || 1) / ratio);
                var skin = opts.skin || "auto";
                var iconAdd = opts.iconAdd || $input.data("icon-add") || "layui-icon layui-icon-addition";
                var iconSubtract = opts.iconSubtract || $input.data("icon-subtract") || "layui-icon layui-icon-subtraction";
                var width = opts.width || $input.data("width") || 100;
                var allowEmpty = opts.allowEmpty || hasAttr($input, "data-allow-empty") || false;
                var autoSelect = opts.autoSelect || hasAttr($input, "data-auto-select") || false;
                var setDefaultValue = opts.defaultValue || $input.attr("value") || (hasAttr($input, "data-default-value") ? $input.attr("data-default-value") : "");
                var defaultEmptyValue = opts.defaultEmptyValue || false;
                var defaultValue = ratioNumber(parseFloat(setDefaultValue) || 0, precision);
                if (defaultEmptyValue && setDefaultValue.length == 0) defaultValue = "";
                var readonly = opts.readonly || $input.attr("readonly") || false;
                var disabled = opts.disabled || $input.attr("disabled") || false;

                // 激活指定事件
                function activeEvent(name, event, dom, value, tree) {
                    if (opts.event && typeof opts.event[name] == "function") {
                        return opts.event[name](event, dom, value, tree);
                    } else {
                        return false;
                    }
                }

                // 开始组装DOM
                activeEvent("beforeCreated");

                // 绑定元素
                var $container = $('<div class="' + createClassList("container", skin) + '"></div>');
                var $subtract = $('<div class="' + createClassList("subtract", skin) + '"></div>').append($('<i class="' + iconSubtract + '"></i>'));
                var $add = $('<div class="' + createClassList("add", skin) + '"></div>').append($('<i class="' + iconAdd + '"></i>'));
                $input.addClass(createClassList("input", skin));

                // DOM集合
                var $dom = {
                    input: $input,
                    subtract: $subtract,
                    add: $add,
                    container: $container,
                };

                // 自动全选
                $input.on("focus", function (event) {
                    activeEvent("focus", event, $input, $input.val(), $dom);
                    if (autoSelect) $input.select()
                });
                $input.on("select", function (event) {
                    activeEvent("select", event, $input, $input.val(), $dom);
                });

                // 处理逻辑
                $input
                    .val(defaultValue)
                    .css({
                        width: width,
                    })
                    .attr({
                        readonly: readonly,
                        disabled: disabled
                    })
                    .on("input", function (event) {
                        var val = $(this).val();
                        var newVal = val;

                        // 如果精度大于0
                        if (precision > 0) {
                            newVal = val.replace(/[^\d\-\.]/g, "");
                            newVal = newVal.replace(/^\./, "");
                            newVal = newVal.replace(/\.{2,}$/g, ".");
                            newVal = newVal.replace(/\-\./, "-");
                            newVal = newVal.replace(new RegExp("\\.(\\d{" + precision + "})\\d+", "g"), ".$1");
                        } else {
                            newVal = newVal.replace(/[^\d\-]/g, "");
                            if (min >= 0) {
                                newVal = newVal.replace(/^0([0-9])/, "$1");
                            } else {
                                newVal = newVal.replace(/\-0{2}/, "-0");
                                newVal = newVal.replace(/\-0([0-9])/, "-$1");
                            }
                        }

                        // 不允许输入以0开头的整数
                        newVal = newVal.replace(/^0+/g, "0");
                        $(this).val(newVal);

                        // 如果最小值小于零则不允许输入负数
                        if (min >= 0) {
                            newVal = newVal.replace(/\-/g, "");
                        }

                        // 只允许头部出现负号
                        var values = newVal.split("");
                        values.forEach(function (val, i) {
                            if (val === "-" && i !== 0) {
                                values.splice(i, 1);
                            }
                        });

                        newVal = values.join("");

                        if (newVal) {
                            var floatVal = parseFloat(newVal);

                            if (floatVal > max) {
                                var maxVal = ratioNumber(max, precision);
                                $(this).val(maxVal);
                                activeEvent("change", event, $input, maxVal, $dom);
                                activeEvent("input", event, $input, maxVal, $dom);
                                return;
                            }
                            if (floatVal < min) {
                                var minVal = ratioNumber(min, precision);
                                $(this).val(minVal);
                                activeEvent("change", event, $input, minVal, $dom);
                                activeEvent("input", event, $input, minVal, $dom);
                                return;
                            }
                        }

                        $(this).val(newVal);
                        activeEvent("change", event, $input, val, $dom);
                        activeEvent("input", event, $input, val, $dom);
                    })
                    .on("blur", function (event) {
                        var val = $(this).val();
                        if (val.length === 0) {
                            if (allowEmpty) {
                                activeEvent("change", event, $input, val, $dom);
                                activeEvent("blur", event, $input, val, $dom);
                                return;
                            } else {
                                var newVal = ratioNumber(defaultValue, precision);
                                $(this).val(newVal);
                                activeEvent("change", event, $input, newVal, $dom);
                                activeEvent("blur", event, $input, newVal, $dom);
                                return;
                            }
                        } else {
                            var newVal = ratioNumber(val, precision);
                            $(this).val(newVal);
                            activeEvent("change", event, $input, newVal, $dom);
                            activeEvent("blur", event, $input, newVal, $dom);
                            return;
                        }
                    })
                    .on("keypress", function (event) {
                        activeEvent("keypress", event, $input, $input.val(), $dom);
                    }).on("mousewheel", function (event) {
                    activeEvent("mousewheel", event, $input, $input.val(), $dom);
                });

                $subtract.on("click", function (event) {
                    if(readonly || disabled) return;

                    var val = $input.val();
                    var floatVal = parseFloat(val);

                    if (floatVal - step >= min) {
                        var newVal = ratioNumber(floatVal - step, precision);
                        $input.val(newVal);
                        if (floatVal != min) {
                            activeEvent("change", event, $subtract, newVal, $dom);
                        } else {
                            activeEvent("toMin", event, $subtract, newVal, $dom);
                        }
                    } else {
                        var newVal = ratioNumber(min, precision);
                        $input.val(newVal);
                        if (floatVal == min) {
                            activeEvent("toMin", event, $subtract, newVal, $dom);
                        } else {
                            activeEvent("change", event, $subtract, newVal, $dom);
                        }
                    }
                });

                $add.on("click", function (event) {
                    
                    if(readonly || disabled) return;

                    var val = $input.val();

                    var floatVal = parseFloat(val);

                    if (floatVal + step <= max) {
                        var newVal = ratioNumber(floatVal + step, precision);
                        $input.val(newVal);
                        activeEvent("change", event, $add, newVal, $dom);
                    } else {
                        var newVal = ratioNumber(max, precision);
                        $input.val(newVal);
                        if (floatVal == max) {
                            activeEvent("toMax", event, $add, newVal, $dom);
                        } else {
                            activeEvent("change", event, $add, newVal, $dom);
                        }
                    }
                });

                $container.append($subtract).append($input).append($add);

                activeEvent("created", $dom);
                $target.empty().append($container);
                activeEvent("mounted", $dom);
            })($(this))
        })
    }

    exports("numberInput", {
        render: render
    });
});