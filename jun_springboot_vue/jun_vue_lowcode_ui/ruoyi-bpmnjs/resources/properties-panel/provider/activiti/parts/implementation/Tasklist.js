'use strict';

var entryFactory = require('../../../../factory/EntryFactory');

var cmdHelper = require('../../../../helper/CmdHelper');

module.exports = function(element, bpmnFactory, options, translate) {

  var getBusinessObject = options.getBusinessObject;

  var isStartableInTasklistEntry = entryFactory.checkbox({
    id: 'isStartableInTasklist',
    label: translate('Startable'),
    modelProperty: 'isStartableInTasklist',

    get: function(element, node) {
      var bo = getBusinessObject(element);
      var isStartableInTasklist = bo.get('activiti:isStartableInTasklist');

      return {
        isStartableInTasklist: isStartableInTasklist ? isStartableInTasklist : ''
      };
    },

    set: function(element, values) {
      var bo = getBusinessObject(element);
      return cmdHelper.updateBusinessObject(element, bo, {
        'activiti:isStartableInTasklist': !!values.isStartableInTasklist
      });
    }

  });

  return [
    isStartableInTasklistEntry
  ];
};
