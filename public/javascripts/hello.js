if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

function disable(page,total){
    
    if(page<1){
        $('#prev').attr('disabled','disabled');
        
    }
    else{
        $('#prev').removeAttr('disabled');
    }
    if((page+1)*5>=total){
        $('#next').attr('disabled','disabled');
    }
    else{
        $('#next').removeAttr('disabled');
    }
}

function search(){
    var search = $('#searchBox').val();
    window.location.href = "/show?search="+search;
    
}



/**
 * Validation for Sign In Form
 */
(function($, W, D) {
	var JQUERY4U = {};

	JQUERY4U.UTIL = {
		setupFormValidation : function()
		{
			//form validation rules
			$("#logInForm").validate(
							{
								rules : {
									emailid : {
										required : true,
										email : true
									},
									password : {
										required : true,
										minlength : 5
									},
								},
								messages : {
									password : {
										required : "Please provide a password",
										minlength : "Password must be at least 5 char long"
									},
									emailid : "Please enter a valid email address",
								},
								submitHandler : function(
										form) {
									form.submit();
								}
							});
		}
	}

	//when the dom has loaded setup form validation rules
	$(D).ready(function($) {
		JQUERY4U.UTIL.setupFormValidation();
	});

})(jQuery, window, document);



(function($,W,D)
        {
            var JQUERY4U = {};

            JQUERY4U.UTIL =
            {
                setupFormValidation: function()
                {
                    //form validation rules
                    $("#signUpForm").validate({
                        rules: {
                            name:{
                                required: true,
                            },
                            address:{
                                required: true,
                            },
                            company:{
                                required: true,
                            },
                            emailid: {
                                required: true,
                                email: true
                            },
                            password: {
                                required: true,
                                minlength: 5
                            },
                            phone:{
                                required: true
                            }
                            
                        },
                        messages: {
                            name:{
                                required: "Please provide a name",
                            },
                            address:{
                                required: "Please provide an address",
                            },
                            company:{
                                required: "Please provide a company",
                            },
                            password: {
                                required: "Please provide a password",
                                minlength: "Password must be at least 5 char long"
                            },
                            emailid: {
                                required: "Please enter an email address",
                                email: "Please enter a valid email address"
                            },
                            phone:{
                                required: "Please enter a phone number."
                                
                            }
                             
                        },
                        submitHandler: function(form) {
                            form.submit();
                        }
                    });
                }
            }

            //when the dom has loaded setup form validation rules
            $(D).ready(function($) {
                JQUERY4U.UTIL.setupFormValidation();
            });

        })(jQuery, window, document);
