$(function () {

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		studentGrade: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},

		getInfo: function(id){
			$.get(baseURL + "sport/studentgrade/info/"+id, function(r){
                vm.studentGrade = r.studentGrade;
            });
		},

	}
});