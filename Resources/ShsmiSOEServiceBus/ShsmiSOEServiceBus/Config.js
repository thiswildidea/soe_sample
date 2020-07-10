dojo.provide("arcgis.soe.ShsmiSOEServiceBus.ShsmiSOEServiceBus.Config");

dojo.require("dijit._Templated");

dojo.require("esri.discovery.dijit.services._CustomSoeConfigurationPane");

dojo.declare("arcgis.soe.ShsmiSOEServiceBus.ShsmiSOEServiceBus.Config", [ esri.discovery.dijit.services._CustomSoeConfigurationPane, dijit._Templated ], {
  
  templatePath: dojo.moduleUrl("arcgis.soe.ShsmiSOEServiceBus.ShsmiSOEServiceBus", "templates/ShsmiSOEServiceBus.html"),
  widgetsInTemplate: true,
  typeName: "ShsmiSOEServiceBus",
  _capabilities: null,

  // some UI element references...
  _setProperties: function(extension) {
	this.inherited(arguments); 
    this.set({
      	test:extension.properties.test

    });
  },
  
  getProperties: function() {
	  var myCustomSoeProps = {
      properties: {
    		test:this.get("test")
      }
    };

    return dojo.mixin(this.inherited(arguments), myCustomSoeProps);
  }
  
  //Create setters and getters for all properties
});