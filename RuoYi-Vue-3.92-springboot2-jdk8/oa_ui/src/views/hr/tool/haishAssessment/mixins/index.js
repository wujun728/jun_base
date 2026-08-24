const data = {
  // 海事量表基础数据
  data() {
    // 人际关系技巧
    this.professionalKnowledge = [
      {
        id: 1,
        label: '基本的',
      },
      {
        id: 2,
        label: '初等业务的',
      },
      {
        id: 3,
        label: '中等业务的',
      },
      {
        id: 4,
        label: '高等业务的',
      },
      {
        id: 5,
        label: '基本专门技术的',
      },
      {
        id: 6,
        label: '熟练专门技术的',
      },
      {
        id: 7,
        label: '精通专门技术的',
      },
      {
        id: 8,
        label: '权威专门技术的',
      },
    ];

    // 管理诀窍
    this.managementKnowHow = [
      {
        id: 1,
        label: '起码的',
      },
      {
        id: 2,
        label: '有关的',
      },
      {
        id: 3,
        label: '多样的',
      },
      {
        id: 4,
        label: '广博的',
      },
      {
        id: 5,
        label: '全面的',
      },
    ];

    // 智能水平分析数据
    this.interpersonalRelationship =[ 
     
        {
          id: 1,
          label: '基本的',
        },
        {
          id: 2,
          label: '重要的',
        },
        {
          id: 3,
          label: '关键的',
        },
  
      ]

    // 智能水平分析评分 
    this.score = {
      111: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      112: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      112: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      113: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      121: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      122: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      123: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      131: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      132: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      133: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      141: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      142: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      143: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      151: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      152: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      153: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],

      211: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      212: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      213: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      221: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      222: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      223: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      231: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      232: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      233: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      241: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      242: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      243: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      251: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      252: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      253: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],

      311: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      312: [{id: 1,label:100},{id: 2,label: 115},{id: 3,label: 132}],
      313: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      321: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      322: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      323: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      331: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      332: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      333: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      341: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      342: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      343: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      351: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      352: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      353: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],

      411: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      412: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      413: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      421: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      422: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      423: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      431: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      432: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      433: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      441: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      442: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      443: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      451: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      452: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      453: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],

      511: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      512: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      513: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      521: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      522: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      523: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      531: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      532: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      533: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      541: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      542: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      543: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      551: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      552: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      553: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],

      611: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      612: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      613: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      621: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      622: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      623: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      631: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      632: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      633: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      641: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      642: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      643: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      651: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      652: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      653: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],

      711: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      712: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      713: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      721: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      722: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      723: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      731: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      732: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      733: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      741: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      742: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      743: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      751: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      752: [{id: 1,label: 920},{id: 2,label: 1056},{id: 3,label: 1216}],
      753: [{id: 1,label: 1056},{id: 2,label: 1216},{id: 3,label: 1400}],

      811: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      812: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      813: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 600}],
      821: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 600}],
      822: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      823: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      831: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      832: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      833: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      841: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      842: [{id: 1,label: 920},{id: 2,label: 1056},{id: 3,label: 1216}],
      843: [{id: 1,label: 1056},{id: 2,label: 1216},{id: 3,label: 1400}],
      851: [{id: 1,label: 1056},{id: 2,label: 1216},{id: 3,label: 1400}],
      852: [{id: 1,label: 1216},{id: 2,label: 1400},{id: 3,label: 1600}],
      853: [{id: 1,label: 1400},{id: 2,label: 1600},{id: 3,label: 1840}],
    };

    // 思维环境
    this.environment = [
      {
        id: 1,
        label: '高度常规性的',
      },
      {
        id: 2,
        label: '常规性的',
      },
      {
        id: 3,
        label: '半常规性的',
      },
      {
        id: 4,
        label: '标准化的',
      },
      {
        id: 5,
        label: '明确规定的',
      },
      {
        id: 6,
        label: '广泛规定的',
      },
      {
        id: 7,
        label: '一般规定的',
      },
      {
        id: 8,
        label: '抽象规定的',
      },
    ]

    // 思维难度
    this.difficulty = [
    {
      id: 1,
      label: '重复性的'
    },
    {
      id: 2,
      label: '模式化的'
    },
    {
      id: 3,
      label: '中间型的'
    },
    {
      id: 4,
      label: '适应性的'
    },
    {
      id: 5,
      label: '无先例的'
    },
   
    ]

    // 解决问题能力评分
    this.solve = {
      11: [{id: 1,label: '10%'},{id: 2,label: '12%'}],
      12: [{id: 1,label: '14%'},{id: 2,label: '16%'}],
      13: [{id: 1,label: '19%'},{id: 2,label: '22%'}],
      14: [{id: 1,label: '25%'},{id: 2,label: '29%'}],
      15: [{id: 1,label: '33%'},{id: 2,label: '38%'}],

      21: [{id: 1,label: '12%'},{id: 2,label: '14%'}],
      22: [{id: 1,label: '16%'},{id: 2,label: '19%'}],
      23: [{id: 1,label: '22%'},{id: 2,label: '25%'}],
      24: [{id: 1,label: '29%'},{id: 2,label: '33%'}],
      25: [{id: 1,label: '38%'},{id: 2,label: '43%'}],

      31: [{id: 1,label: '14%'},{id: 2,label: '16%'}],
      32: [{id: 1,label: '19%'},{id: 2,label: '22%'}],
      33: [{id: 1,label: '25%'},{id: 2,label: '29%'}],
      34: [{id: 1,label: '33%'},{id: 2,label: '38%'}],
      35: [{id: 1,label: '43%'},{id: 2,label: '50%'}],

      41: [{id: 1,label: '16%'},{id: 2,label: '19%'}],
      42: [{id: 1,label: '22%'},{id: 2,label: '25%'}],
      43: [{id: 1,label: '29%'},{id: 2,label: '33%'}],
      44: [{id: 1,label: '38%'},{id: 2,label: '43%'}],
      45: [{id: 1,label: '50%'},{id: 2,label: '57%'}],

      51: [{id: 1,label: '19%'},{id: 2,label: '22%'}],
      52: [{id: 1,label: '25%'},{id: 2,label: '29%'}],
      53: [{id: 1,label: '33%'},{id: 2,label: '38%'}],
      54: [{id: 1,label: '43%'},{id: 2,label: '50%'}],
      55: [{id: 1,label: '57%'},{id: 2,label: '66%'}],

      61: [{id: 1,label: '22%'},{id: 2,label: '25%'}],
      62: [{id: 1,label: '29%'},{id: 2,label: '33%'}],
      63: [{id: 1,label: '38%'},{id: 2,label: '43%'}],
      64: [{id: 1,label: '50%'},{id: 2,label: '57%'}],
      65: [{id: 1,label: '66%'},{id: 2,label: '76%'}],

      71: [{id: 1,label: '25%'},{id: 2,label: '29%'}],
      72: [{id: 1,label: '33%'},{id: 2,label: '38%'}],
      73: [{id: 1,label: '43%'},{id: 2,label: '50%'}],
      74: [{id: 1,label: '57%'},{id: 2,label: '66%'}],
      75: [{id: 1,label: '76%'},{id: 2,label: '87%'}],

      81: [{id: 1,label: '29%'},{id: 2,label: '38%'}],
      82: [{id: 1,label: '38%'},{id: 2,label: '43%'}],
      83: [{id: 1,label: '50%'},{id: 2,label: '57%'}],
      84: [{id: 1,label: '66%'},{id: 2,label: '76%'}],
      85: [{id: 1,label: '87%'},{id: 2,label: '100%'}],
      
    }

    // 行动自由度
    this.free = [
      {
        id: 1,
        label: '有规定的'
      },
      {
        id: 2,
        label: '受控制的'
      },
      {
        id: 3,
        label: '标准化的'
      },
      {
        id: 4,
        label: '一般性规范的'
      },
      {
        id: 5,
        label: '有指导的'
      },
      {
        id: 6,
        label: '方向性指导的'
      },
      {
        id: 7,
        label: '广泛性指引的'
      },
      {
        id: 8,
        label: '战略性指引的'
      },
      {
        id: 9,
        label: '一般性无指引的'
      },
    ]

    // 职位责任
    this.responsibility = [
      {
        id: 1,
        label: '微小'
      },
      {
        id: 2,
        label: '少量'
      },
      {
        id: 3,
        label: '中级'
      },
      {
        id: 4,
        label: '大量'
      },
    ]

    // 职位影响结果
    this.influence = [
    {
      value:1,
    label:'间接',
    children:[
      {
        value:1,
    label:'后勤'
    },
    {
      value:2,
      label:'辅助'
    }]
    },
    {
      value:2,
    label:'直接',
    children:[{
      value:1,
    label:'分摊'
    },
    {
      value:2,
      label:'主要'
    }]
    },
    ]

    // 承担责任分析评分
    this.bear = {
      1111: [{id: 1,label: 10},{id: 2,label: 12},{id: 3,label: 14}],
      1112: [{id: 1,label: 14},{id: 2,label: 16},{id: 3,label: 18}],
      1121: [{id: 1,label: 19},{id: 2,label: 22},{id: 3,label: 25}],
      1122: [{id: 1,label: 25},{id: 2,label: 29},{id: 3,label: 33}],
      1211: [{id: 1,label: 14},{id: 2,label: 16},{id: 3,label: 19}],
      1212: [{id: 1,label: 19},{id: 2,label: 22},{id: 3,label: 25}],
      1221: [{id: 1,label: 25},{id: 2,label: 29},{id: 3,label: 33}],
      1222: [{id: 1,label: 33},{id: 2,label: 38},{id: 3,label: 43}],
      1311: [{id: 1,label: 19},{id: 2,label: 22},{id: 3,label: 25}],
      1312: [{id: 1,label: 25},{id: 2,label: 29},{id: 3,label: 33}],
      1321: [{id: 1,label: 33},{id: 2,label: 38},{id: 3,label: 43}],
      1322: [{id: 1,label: 43},{id: 2,label: 50},{id: 3,label: 57}],
      1411: [{id: 1,label: 25},{id: 2,label: 29},{id: 3,label: 33}],
      1412: [{id: 1,label: 33},{id: 2,label: 38},{id: 3,label: 43}],
      1421: [{id: 1,label: 43},{id: 2,label: 50},{id: 3,label: 57}],
      1422: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],

      2111: [{id: 1,label: 16},{id: 2,label: 19},{id: 3,label: 22}],
      2112: [{id: 1,label: 22},{id: 2,label: 25},{id: 3,label: 29}],
      2121: [{id: 1,label: 29},{id: 2,label: 33},{id: 3,label: 38}],
      2122: [{id: 1,label: 38},{id: 2,label: 43},{id: 3,label: 50}],
      2211: [{id: 1,label: 22},{id: 2,label: 25},{id: 3,label: 29}],
      2212: [{id: 1,label: 29},{id: 2,label: 33},{id: 3,label: 38}],
      2221: [{id: 1,label: 38},{id: 2,label: 43},{id: 3,label: 50}],
      2222: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      2311: [{id: 1,label: 29},{id: 2,label: 33},{id: 3,label: 38}],
      2312: [{id: 1,label: 38},{id: 2,label: 43},{id: 3,label: 50}],
      2321: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      2322: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      2411: [{id: 1,label: 38},{id: 2,label: 43},{id: 3,label: 50}],
      2412: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      2421: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      2422: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],

      3111: [{id: 1,label: 25},{id: 2,label: 29},{id: 3,label: 33}],
      3112: [{id: 1,label: 33},{id: 2,label: 38},{id: 3,label: 43}],
      3121: [{id: 1,label: 43},{id: 2,label: 50},{id: 3,label: 57}],
      3122: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      3211: [{id: 1,label: 33},{id: 2,label: 38},{id: 3,label: 43}],
      3212: [{id: 1,label: 43},{id: 2,label: 50},{id: 3,label: 57}],
      3221: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      3222: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      3311: [{id: 1,label: 43},{id: 2,label: 50},{id: 3,label: 57}],
      3312: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      3321: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      3322: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      3411: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      3412: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      3421: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      3422: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],

      4111: [{id: 1,label: 38},{id: 2,label: 43},{id: 3,label: 50}],
      4112: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      4121: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      4122: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      4211: [{id: 1,label: 50},{id: 2,label: 57},{id: 3,label: 66}],
      4212: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      4221: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      4222: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      4311: [{id: 1,label: 66},{id: 2,label: 76},{id: 3,label: 87}],
      4312: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      4321: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      4322: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      4411: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 115}],
      4412: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      4421: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      4422: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],

      5111: [{id: 1,label: 57},{id: 2,label: 66},{id: 3,label: 76}],
      5112: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      5121: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      5122: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      5211: [{id: 1,label: 76},{id: 2,label: 87},{id: 3,label: 100}],
      5212: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      5221: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      5222: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      5311: [{id: 1,label: 100},{id: 2,label: 115},{id: 3,label: 132}],
      5312: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      5321: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      5322: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      5411: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      5412: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      5421: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      5422: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],

      6111: [{id: 1,label: 87},{id: 2,label: 100},{id: 3,label: 175}],
      6112: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      6121: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      6122: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      6211: [{id: 1,label: 115},{id: 2,label: 132},{id: 3,label: 152}],
      6212: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      6221: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      6222: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      6311: [{id: 1,label: 152},{id: 2,label: 175},{id: 3,label: 200}],
      6312: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      6321: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      6322: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      6411: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      6412: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      6421: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      6422: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],

      7111: [{id: 1,label: 132},{id: 2,label: 152},{id: 3,label: 175}],
      7112: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      7121: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      7122: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      7211: [{id: 1,label: 175},{id: 2,label: 200},{id: 3,label: 230}],
      7212: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      7221: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      7222: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      7311: [{id: 1,label: 230},{id: 2,label: 264},{id: 3,label: 304}],
      7312: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      7321: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      7322: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      7411: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      7412: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      7421: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      7422: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],

      8111: [{id: 1,label: 200},{id: 2,label: 230},{id: 3,label: 264}],
      8112: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      8121: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      8122: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      8211: [{id: 1,label: 264},{id: 2,label: 304},{id: 3,label: 350}],
      8212: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      8221: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      8222: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      8311: [{id: 1,label: 350},{id: 2,label: 400},{id: 3,label: 460}],
      8312: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      8321: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      8322: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      8411: [{id: 1,label: 460},{id: 2,label: 528},{id: 3,label: 608}],
      8412: [{id: 1,label: 608},{id: 2,label: 700},{id: 3,label: 800}],
      8421: [{id: 1,label: 800},{id: 2,label: 920},{id: 3,label: 1056}],
      8422: [{id: 1,label: 1056},{id: 2,label: 1216},{id: 3,label: 1400}],

      
      9111: [{id: 1,label: 304},{id: 2,label: 350},{id: 3,label: 400}],
      9112: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      9121: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      9122: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      9211: [{id: 1,label: 400},{id: 2,label: 460},{id: 3,label: 528}],
      9212: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      9221: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      9222: [{id: 1,label: 920},{id: 2,label: 1056},{id: 3,label: 1216}],
      9311: [{id: 1,label: 528},{id: 2,label: 608},{id: 3,label: 700}],
      9312: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      9321: [{id: 1,label: 920},{id: 2,label: 1056},{id: 3,label: 1216}],
      9322: [{id: 1,label: 1216},{id: 2,label: 1400},{id: 3,label: 1600}],
      9411: [{id: 1,label: 700},{id: 2,label: 800},{id: 3,label: 920}],
      9412: [{id: 1,label: 920},{id: 2,label: 1056},{id: 3,label: 1216}],
      9421: [{id: 1,label: 1216},{id: 2,label: 1400},{id: 3,label: 1600}],
      9422: [{id: 1,label: 1600},{id: 2,label: 1840},{id: 3,label: 2112}],
    }

  },
};

export default data;
