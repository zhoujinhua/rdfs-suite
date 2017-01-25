(function($){
		     //��չ��ѧ����
		    $.Math = {
		        //������λС��   
		        //���ܣ����������������룬ȡС�����2λ  
		        toDecimal : function (x) {
		            var f = parseFloat(x);
		            if (isNaN(f)) {
		                return;
		            }
		            f = Math.round(x * 100) / 100;
		            return f;
		        },
		        //ǿ�Ʊ���2λС�����磺2������2���油��00.��2.00  
		        toDecimal2 : function (x) {
		            var f = parseFloat(x);
		            if (isNaN(f)) {
		                return false;
		            }
		            var f = Math.round(x * 100) / 100;
		            var s = f.toString();
		            var rs = s.indexOf('.');
		            if (rs < 0) {
		                rs = s.length;
		                s += '.';
		            }
		            while (s.length <= rs + 2) {
		                s += '0';
		            }
		            return s;
		        },
		        //��������λС��
		        fomatFloat : function (src, pos) {
		            return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
		        }
		    }
		    //���븨��
		    $.Input = {
		        //�����޶�,ָ���Ƿ�����Ϊ����
		        BoundInt: function (input_id,negative) {
		            var allowkeycode = [8, 37, 39,  48, 49, 50, 51, 52, 53, 54, 55, 56, 57];
		            $(input_id).bind("keypress", function (e) {
		                var event = e || window.event;
		                if (!event.keyCode || event.keyCode == 0) {
		                    event.keyCode = event.which;
		                }
		                var value = $(this).val();
		                 if (allowkeycode.indexOf(event.keyCode) >= 0) {
		                    return true;
		                 } else {
		                     //������
		                     if (negative&&event.keyCode==45) {
		                         if (value.length == 0 || !(value.indexOf('-') >= 0)) {
		                             return true;
		                         }
		                     }
		                    return false;
		                }
		            }).focusout(function () {
		                var input = $(this).val();
		                var num = parseInt(input,10);
		                if (isNaN(num)) {
		                    num = 0;
		                }
		                $(this).val(num);
		            }).css({"ime-mode":  "disabled"});
		        },
		        //С���޶�������ָ������С��λ,�Ƿ�������
		        BoundFloat: function (input_id, pos, negative) {
		            var allowkeycode = [8,37,39,46,48,49,50,51,52,53,54,55,56,57];
		            $(input_id).bind("keypress", function (e) {
		                var event = e || window.event;
		                if (!event.keyCode || event.keyCode == 0) {
		                    event.keyCode = event.which;
		                }
		                var value = $(this).val();
		                if (allowkeycode.indexOf(event.keyCode)>=0) {
		                    return true;
		                } else {
		                    //������
		                    if (negative && event.keyCode == 45) {
		                        if (value.length == 0||!(value.indexOf('-')>=0)) {
		                            return true;
		                        }
		                    }
		                    return false;
		                }
		            }).focusout(function () {
		                var input = $(this).val();
		                var num = parseFloat(input);
		                if (isNaN(num)) {
		                    num = 0;
		                }
		                if (pos) {
		                    num = $.Math.fomatFloat(num, pos);
		                }
		                $(this).val(num);
		            }).css({"ime-mode":  "disabled"});
		        }
		    }
		})(jQuery);