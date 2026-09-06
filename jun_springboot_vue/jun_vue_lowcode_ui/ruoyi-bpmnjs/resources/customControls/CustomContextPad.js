export default class CustomContextPad {
  constructor(config, contextPad, create, elementFactory, injector, translate) {
    this.create = create;
    this.elementFactory = elementFactory;
    this.translate = translate;
    //自动摆放位置
    if (config.autoPlace !== false) {
      this.autoPlace = injector.get('autoPlace', false);
    }
    //注册工具
    contextPad.registerProvider(this);
  }
  getContextPadEntries(element) {
    const {
      autoPlace,
      create,
      elementFactory,
      translate
    } = this;

    function appendUserTask(event, element) {
      if (autoPlace) {
        const shape = elementFactory.createShape({ type: 'bpmn:UserTask' });
        autoPlace.append(element, shape);
      } else {
        appendUserTaskStart(event, element);
      }
    }

    function appendUserTaskStart(event) {
          const shape = elementFactory.createShape({ type: 'bpmn:UserTask' });
          create.start(event, shape, element);
    }
    function appendCallActivityStart(event) {
      const shape = elementFactory.createShape({ type: 'bpmn:CallActivity' });
      create.start(event, shape, element);
    }

    function appendCallActivity(event, element) {
        if (autoPlace) {
            const shape = elementFactory.createShape({ type: 'bpmn:CallActivity' });
            autoPlace.append(element, shape);
        } else {
            appendCallActivityStart(event, element);
        }
    }
    return {
      'append.user-task': {
        group: 'model',
        className: 'bpmn-icon-user-task',
        title: translate('Append ServiceTask'),
        action: {
          click: appendUserTask,
          dragstart: appendUserTaskStart
        }
      },
      'append.call-activity':{
          group: 'model',
          className: 'bpmn-icon-call-activity',
          title: translate('Append CallActivity'),
          action: {
              click: appendCallActivity,
              dragstart: appendCallActivityStart
          }
      }
    };
  }
}
CustomContextPad.$inject = [
  'config',
  'contextPad',
  'create',
  'elementFactory',
  'injector',
  'translate'
];