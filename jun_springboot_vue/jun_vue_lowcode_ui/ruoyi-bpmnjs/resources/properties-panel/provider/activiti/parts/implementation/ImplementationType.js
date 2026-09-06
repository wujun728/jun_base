'use strict';

var entryFactory = require('../../../../factory/EntryFactory'),
    cmdHelper = require('../../../../helper/CmdHelper'),
    extensionElementsHelper = require('../../../../helper/ExtensionElementsHelper'),
    elementHelper = require('../../../../helper/ElementHelper');

var assign = require('lodash/assign');
var map = require('lodash/map');

var DEFAULT_DELEGATE_PROPS = [ 'class', 'expression', 'delegateExpression' ];

var DELEGATE_PROPS = {
  'activiti:class': undefined,
  'activiti:expression': undefined,
  'activiti:delegateExpression': undefined,
  'activiti:resultVariable': undefined
};

var DMN_CAPABLE_PROPS = {
  'activiti:decisionRef': undefined,
  'activiti:decisionRefBinding': 'latest',
  'activiti:decisionRefVersion': undefined,
  'activiti:mapDecisionResult': 'resultList',
  'activiti:decisionRefTenantId': undefined
};


var EXTERNAL_CAPABLE_PROPS = {
  'activiti:type': undefined,
  'activiti:topic': undefined
};

module.exports = function(element, bpmnFactory, options, translate) {

  var DEFAULT_OPTIONS = [
    { value: 'class', name: translate('Java Class') },
    { value: 'expression', name: translate('Expression') },
    { value: 'delegateExpression', name: translate('Delegate Expression') }
  ];

  var DMN_OPTION = [
    { value: 'dmn', name: translate('DMN') }
  ];

  var EXTERNAL_OPTION = [
    { value: 'external', name: translate('External') }
  ];

  var CONNECTOR_OPTION = [
    { value: 'connector', name: translate('Connector') }
  ];

  var SCRIPT_OPTION = [
    { value: 'script', name: translate('Script') }
  ];

  var getType = options.getImplementationType,
      getBusinessObject = options.getBusinessObject;

  var hasDmnSupport = options.hasDmnSupport,
      hasExternalSupport = options.hasExternalSupport,
      hasServiceTaskLikeSupport = options.hasServiceTaskLikeSupport,
      hasScriptSupport = options.hasScriptSupport;

  var entries = [];

  var selectOptions = DEFAULT_OPTIONS.concat([]);

  if (hasDmnSupport) {
    selectOptions = selectOptions.concat(DMN_OPTION);
  }

  if (hasExternalSupport) {
    selectOptions = selectOptions.concat(EXTERNAL_OPTION);
  }

  if (hasServiceTaskLikeSupport) {
    selectOptions = selectOptions.concat(CONNECTOR_OPTION);
  }

  if (hasScriptSupport) {
    selectOptions = selectOptions.concat(SCRIPT_OPTION);
  }

  selectOptions.push({ value: '' });

  entries.push(entryFactory.selectBox({
    id : 'implementation',
    label: translate('Implementation'),
    selectOptions: selectOptions,
    modelProperty: 'implType',

    get: function(element, node) {
      return {
        implType: getType(element) || ''
      };
    },

    set: function(element, values, node) {
      var bo = getBusinessObject(element);
      var oldType = getType(element);
      var newType = values.implType;

      var props = assign({}, DELEGATE_PROPS);

      if (DEFAULT_DELEGATE_PROPS.indexOf(newType) !== -1) {

        var newValue = '';
        if (DEFAULT_DELEGATE_PROPS.indexOf(oldType) !== -1) {
          newValue = bo.get('activiti:' + oldType);
        }
        props['activiti:' + newType] = newValue;
      }

      if (hasDmnSupport) {
        props = assign(props, DMN_CAPABLE_PROPS);
        if (newType === 'dmn') {
          props['activiti:decisionRef'] = '';
        }
      }

      if (hasExternalSupport) {
        props = assign(props, EXTERNAL_CAPABLE_PROPS);
        if (newType === 'external') {
          props['activiti:type'] = 'external';
          props['activiti:topic'] = '';
        }
      }

      if (hasScriptSupport) {
        props['activiti:script'] = undefined;

        if (newType === 'script') {
          props['activiti:script'] = elementHelper.createElement('activiti:Script', {}, bo, bpmnFactory);
        }
      }

      var commands = [];
      commands.push(cmdHelper.updateBusinessObject(element, bo, props));

      if (hasServiceTaskLikeSupport) {
        var connectors = extensionElementsHelper.getExtensionElements(bo, 'activiti:Connector');
        commands.push(map(connectors, function(connector) {
          return extensionElementsHelper.removeEntry(bo, element, connector);
        }));

        if (newType === 'connector') {
          var extensionElements = bo.get('extensionElements');
          if (!extensionElements) {
            extensionElements = elementHelper.createElement('bpmn:ExtensionElements', { values: [] }, bo, bpmnFactory);
            commands.push(cmdHelper.updateBusinessObject(element, bo, { extensionElements: extensionElements }));
          }
          var connector = elementHelper.createElement('activiti:Connector', {}, extensionElements, bpmnFactory);
          commands.push(cmdHelper.addAndRemoveElementsFromList(
            element,
            extensionElements,
            'values',
            'extensionElements',
            [ connector ],
            []
          ));
        }
      }

      return commands;

    }
  }));

  return entries;

};
