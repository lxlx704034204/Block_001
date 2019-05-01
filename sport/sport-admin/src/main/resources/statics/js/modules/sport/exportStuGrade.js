var vm = new Vue({
	el:'#main',
	data:{
		student:{},
		age:null,
		gender:'未知',
		trainGoal:{},
		bmiGradeList:{},
		lastProGradeList:{},
		lastStuGrade:{},
		average:{},
		scoreDesc:null
	},
	mounted: function(){
		var studentId = url("studentId");
		$.get(baseURL + "sport/grade/info?studentId="+studentId, function(r){
			debugger;
			vm.student = r.student;
			vm.age = r.age;
			vm.gender = r.student.gender == 1? '男':'女';
			vm.bmiGradeList = r.bmiGradeList;
			vm.lastBmiGrade = r.lastBmiGrade;
			vm.scoreDesc = r.scoreDesc;
			vm.lastStuGrade = r.lastStuGrade;
			vm.lastProGradeList = r.lastProGradeList;
			vm.trainGoal = r.trainGoal;
		});
	},
	methods: {
		radarChartFun:function () {
			var radarChart = echarts.init(document.getElementById('radarId'));

			var radarOption = {
				title : { },
				tooltip : {
					trigger: 'axis'
				},
				calculable : true,
				polar : [
					{
						name: { show: true,textStyle:{fontSize:16,color:"#32cd32"}},
						indicator : [
							{text : '解决问题', max  : 100},
							{text : '学习能力', max  : 100},
							{text : '综合', max  : 100},
							{text : '技术能力', max  : 100},
							{text : '业务能力', max  : 100},
							{text : '思维模式', max  : 100}
						],center : ['50%','50%'],
						radius : 50 //半径，可放大放小雷达图
					}
				],
				series : [
					{
						name:'',
						type: 'radar',//图表类型 radar为雷达图
						itemStyle: {
							normal: {
								lineStyle: {
									color :"#87cefa",
									width : 2
								},
								areaStyle: {
									color:"#87cefa",
									type: 'default'
								}
							}
						},
						symbolSize :6,
						data : [{
							value:[100,80,80,80,80,80]
						}]
					}
				]
			}
			radarChart.setOption(radarOption);
		},

		barChartFun: function () {
				// 基于准备好的dom，初始化echarts实例
				var barChart = echarts.init(document.getElementById('barChart'));

				// 指定图表的配置项和数据
				var barOption = {
					title: {
						text: 'ECharts 入门示例'
					},
					tooltip: {},
					legend: {
						data:['销量']
					},
					xAxis: {
						data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
					},
					yAxis: {},
					series: [{
						name: '销量',
						type: 'bar',
						data: [5, 20, 36, 10, 10, 20]
					}]
				};

				// 使用刚指定的配置项和数据显示图表。
				barChart.setOption(barOption);
		},

		bmiChartFun: function () {
				// 基于准备好的dom，初始化echarts实例
				var bmiChart = echarts.init(document.getElementById('bmiChart'));

				// 指定图表的配置项和数据
				var bmiOption = {
					title: {
						text: 'ECharts 入门示例'
					},
					tooltip: {},
					legend: {
						data:['销量']
					},
					xAxis: {
						data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
					},
					yAxis: {},
					series: [{
						name: '销量',
						type: 'bar',
						splitNumber:3,
						splitArea:{
							show:true
						},
						data: [5, 20, 36, 10, 10, 20]
					}]
				};

				// 使用刚指定的配置项和数据显示图表。
				bmiChart.setOption(bmiOption);
		}

	}
});