//Object literal syntax
var calculator = {
	add : function(expression) {
		var pieces = expression.split(/,/g);
		var sum = 0;
		for(var i = 0 ; i < pieces.length ; i++)
		{
			if(pieces[i].indexOf('-') > 0)
			{
				var range = pieces[i].split('-');
				for(var j = parseInt(range[0]) ; j <= parseInt(range[1]); j++)
				{
					sum += j ;
				}
			}else{
				sum += parseInt(pieces[i] || 0);
			}
		}
		return sum;
	},
	sub : function(expression) {
		var pieces = expression.split(/,/g);
		return parseInt(pieces[0]) - parseInt(pieces[1]);
	}
};

var mcams = {
	getCAVT : function(a,b,c)
	{
		return Math.max(a,b,c) < 600 ? 2 : 1;
	}
};

var ArrayUtil = {
	contains : function(array, element)
	{
		for(var x=0; x<array.length; x++)
		{
			if(array[x] == element)
				return true;
		}
		return false;
	}
};

MyApp = {
	displayPerson : function(personId) {
		$.getJSON("people.jsp?"+personId, null, MyApp.renderPerson);
	},
	renderPerson: function(data)
	{
		$("#firstname").text(data.Person.Firstname);
		$("#lastname").text(data.Person.Lastname);
	}
};

var hello = function(name) {
	if(name) 
	{
		return "Hello" + ", " + name +"!" ;
	}else{
		return "Hello!";
	}
};

//CreditCard
var CreditCard = {
	cleanNumber: function(number) {
		return number.replace(/[- ]/g,"");
	},
	validNumber: function(number) {
		var total = 0;
		number = this.cleanNumber(number);
		for(var i=number.length-1;i>=0; i--) {
			var n = parseInt(number.substr(i,1));
			if((i+number.length)%2 == 0) {
				n = n*2 > 9 ? n*2 - 9 : n*2;
			}
			total += n;
		};
		return total % 10 == 0;
	}
};

(function($){
	$.fn.validateCreditCardNumber = function() {
		return this.each(function() {
			$(this).blur(function() {
				if(!CreditCard.validNumber(this.value)) {
					$("#" + this.id + "_error").text("Invalid credit card number.");
				}else{
					$("#" + this.id + "_error").text("");
				}
			});
		});
	};
})(jQuery);


var DropDown = function() 
{
	this.result = [];
};

DropDown.prototype =
{
	loadData : function()
	{
		var result = this.result;
		$.getJSON("data.json", function(data) {
			$.each(data, function(idx, opt) {
				result.push(opt);
			});
		});
	}
	,
	loadDropDown : function(dropdown)
	{
		$.getJSON("data.json", function(data) {
			dropdown.loadSelect(data);
		});
	}
};


function setDropDown()
{
	$.getJSON("data.json", function(data) {
		$("#dropdown").loadSelect(data);	
	});
}

(function($){
	$.fn.emptySelect = function() {
		return this.each(function() {
			if(this.tagName == "SELECT") this.options.length = 0;
		});
	};

	$.fn.loadSelect = function(optionDataArray) {
		return this.emptySelect().each(function() {
			var selectElement = this;
			$.each(optionDataArray, function(index, optionData) {
				var option = new Option(optionData.caption, optionData.value);
				selectElement.add(option);
			});
		});
	};
})(jQuery);

