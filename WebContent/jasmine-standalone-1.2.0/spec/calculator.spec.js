describe("Jasmine", function() {
	it("makes testing JavaScript awesome!", function() {
		expect(true).toBeTruthy();
	});
});

describe("Hello", function() {
	it("says hello", function() {
		expect(hello()).toEqual("Hello!");
	});
	it("says hello to someone", function() {
		expect(hello("Tom")).toEqual("Hello, Tom!");
	});
});

describe("getCAVT", function() {
	it("should get",function() {
		expect(mcams.getCAVT(500,0,0)).toEqual(2);
	});
});

describe("ArrayUtil", function() {
	var list = ["A", "B", "C"];
	it("contails", function() {
		expect(ArrayUtil.contains(list,"A")).toEqual(true);
	});
});

describe("CreditCard", function() {
	it("clean number", function() {
		expect(CreditCard.cleanNumber("123 4-5")).toEqual("12345");
	});
	
	it("validates based on mod 10", function() {
		expect(CreditCard.validNumber("4111 1111-1111 1111")).toBeTruthy();
		expect(CreditCard.validNumber("4111 1111-1111 1121")).toBeFalsy();
	});

	it("validates when text field loses focus", function() {
		loadFixtures("order_form.htm");
		$("#card_number").validateCreditCardNumber();
		$("#card_number").val("4111 1111-1111 1121");
		$("#card_number").blur();
		expect($("#card_number_error")).toHaveText("Invalid credit card number.");
		$("#card_number").val("4111 1111-1111 1111");
		$("#card_number").blur();
		expect($("#card_number_error")).toHaveText("");
	});
});

describe("DropDown", function() {
	it("load option", function() {
		spyOn($, "getJSON").andCallFake(function(url, callback) {
			callback([{},{}]);
		});
		var dd = new DropDown();
		dd.loadData();
		expect(dd.result.length).toBe(2);
	});

	it("load select", function() {
		loadFixtures("select.htm");
		$("#dropdown").emptySelect();
		expect($("#dropdown option").length).toBe(0);

		spyOn($, "getJSON").andCallFake(function(url, callback) {
			callback([{},{}]);
		});
		setDropDown();
		expect($("#dropdown option").length).toBe(2);
	});

	it("load select2", function() {
		loadFixtures("select.htm");
		spyOn($, "getJSON").andCallFake(function(url, callback) {
			callback([{},{}]);
		});
		var dd = new DropDown();
		dd.loadDropDown($("#dropdown"));
		expect($("#dropdown option").length).toBe(2);
	});
});

describe("My App", function() {
	describe("when displaying a person", function() {
		it("should retrieve the person", function() {
			spyOn($, "getJSON");
			var personId = 354;
			MyApp.displayPerson(personId);
			expect($.getJSON).toHaveBeenCalledWith("people.jsp?"+personId, null, jasmine.any(Function));
		});
	});
});

describe("calculator", function() {
	describe("basic functionality", function() {
		checkResult('', 0);
		checkResult('42', 42);
	});

	describe("comma or dash separator", function() {
		checkResult("1,2", 3);
		checkResult("1,2,3,4", 10);
		checkResult("1-1000,3",500503);
	});

	describe("Subtraction", function() {
		checkResultSub("1,2",-1);
		checkResultSub("5,4",1);
	});
	
	function checkResultSub(expression, result) {
		it("should evaluate '" + expression + "' to " + result, function() {
			expect(calculator.sub(expression)).toEqual(result);
		});
	}
	
	function checkResult(expression,result) {
		it("should evaluate '" + expression + "' to " + result, function() {
			expect(calculator.add(expression)).toEqual(result);
		});
	}
});