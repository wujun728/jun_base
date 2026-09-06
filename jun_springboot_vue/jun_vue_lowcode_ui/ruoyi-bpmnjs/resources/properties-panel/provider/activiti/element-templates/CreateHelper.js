'use strict';

var assign = require('lodash/assign');

/**
 * Create an input parameter representing the given
 * binding and value.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createInputParameter(binding, value, bpmnFactory) {
  var scriptFormat = binding.scriptFormat,
      parameterValue,
      parameterDefinition;

  if (scriptFormat) {
    parameterDefinition = bpmnFactory.create('activiti:Script', {
      scriptFormat: scriptFormat,
      value: value
    });
  } else {
    parameterValue = value;
  }

  return bpmnFactory.create('activiti:InputParameter', {
    name: binding.name,
    value: parameterValue,
    definition: parameterDefinition
  });
}

module.exports.createInputParameter = createInputParameter;


/**
 * Create an output parameter representing the given
 * binding and value.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createOutputParameter(binding, value, bpmnFactory) {
  var scriptFormat = binding.scriptFormat,
      parameterValue,
      parameterDefinition;

  if (scriptFormat) {
    parameterDefinition = bpmnFactory.create('activiti:Script', {
      scriptFormat: scriptFormat,
      value: binding.source
    });
  } else {
    parameterValue = binding.source;
  }

  return bpmnFactory.create('activiti:OutputParameter', {
    name: value,
    value: parameterValue,
    definition: parameterDefinition
  });
}

module.exports.createOutputParameter = createOutputParameter;


/**
 * Create activiti property from the given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiProperty(binding, value, bpmnFactory) {
  return bpmnFactory.create('activiti:Property', {
    name: binding.name,
    value: value || ''
  });
}

module.exports.createActivitiProperty = createActivitiProperty;


/**
 * Create activiti:in element from given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiIn(binding, value, bpmnFactory) {

  var properties = createActivitiInOutAttrs(binding, value);

  return bpmnFactory.create('activiti:In', properties);
}

module.exports.createActivitiIn = createActivitiIn;


/**
 * Create activiti:in with businessKey element from given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiInWithBusinessKey(binding, value, bpmnFactory) {
  return bpmnFactory.create('activiti:In', {
    businessKey: value
  });
}

module.exports.createActivitiInWithBusinessKey = createActivitiInWithBusinessKey;


/**
 * Create activiti:out element from given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiOut(binding, value, bpmnFactory) {
  var properties = createActivitiInOutAttrs(binding, value);

  return bpmnFactory.create('activiti:Out', properties);
}

module.exports.createActivitiOut = createActivitiOut;


/**
 * Create activiti:executionListener element containing an inline script from given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiExecutionListenerScript(binding, value, bpmnFactory) {
  var scriptFormat = binding.scriptFormat,
      parameterValue,
      parameterDefinition;

  if (scriptFormat) {
    parameterDefinition = bpmnFactory.create('activiti:Script', {
      scriptFormat: scriptFormat,
      value: value
    });
  } else {
    parameterValue = value;
  }

  return bpmnFactory.create('activiti:ExecutionListener', {
    event: binding.event,
    value: parameterValue,
    script: parameterDefinition
  });
}

module.exports.createActivitiExecutionListenerScript = createActivitiExecutionListenerScript;

/**
 * Create activiti:field element containing string or expression from given binding.
 *
 * @param {PropertyBinding} binding
 * @param {String} value
 * @param {BpmnFactory} bpmnFactory
 *
 * @return {ModdleElement}
 */
function createActivitiFieldInjection(binding, value, bpmnFactory) {
  var DEFAULT_PROPS = {
    'string': undefined,
    'expression': undefined,
    'name': undefined
  };

  var props = assign({}, DEFAULT_PROPS);

  if (!binding.expression) {
    props.string = value;
  } else {
    props.expression = value;
  }
  props.name = binding.name;

  return bpmnFactory.create('activiti:Field', props);
}
module.exports.createActivitiFieldInjection = createActivitiFieldInjection;


// helpers ////////////////////////////

/**
 * Create properties for activiti:in and activiti:out types.
 */
function createActivitiInOutAttrs(binding, value) {

  var properties = {};

  // activiti:in source(Expression) target
  if (binding.target) {

    properties.target = binding.target;

    if (binding.expression) {
      properties.sourceExpression = value;
    } else {
      properties.source = value;
    }
  } else

  // activiti:(in|out) variables local
  if (binding.variables) {
    properties.variables = 'all';

    if (binding.variables === 'local') {
      properties.local = true;
    }
  }

  // activiti:out source(Expression) target
  else {
    properties.target = value;

    [ 'source', 'sourceExpression' ].forEach(function(k) {
      if (binding[k]) {
        properties[k] = binding[k];
      }
    });
  }

  return properties;
}
