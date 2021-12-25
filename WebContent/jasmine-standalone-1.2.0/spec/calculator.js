var calculator = {
	add : function(expression) {
		var pieces = expression.split(/,|-/g);
		var sum = 0;
		for(var i = 0 ; i < pieces.length ; i++)
		{
			sum += parseInt(pieces[i] || 0);
		}
		return sum;
	}
};
