(function($) {
  $.fn.emptySelect = function() {
    return this.each(function(){
      if (this.tagName=='SELECT') this.options.length = 0;
    });
  }

  $.fn.loadSelect = function(optionsDataArray) {
    return this.emptySelect().each(function(){
      if (this.tagName=='SELECT') {
        var selectElement = this;
        $.each(optionsDataArray,function(index,optionData){
          var option = new Option(optionData.Name,
                                  optionData.ID);
          if ($.browser.msie) {
            selectElement.add(option);
          }
          else {
            selectElement.add(option,null);
          }
        });
      }
    });
  }
})(jQuery);
