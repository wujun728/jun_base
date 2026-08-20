var SQL_KEYS = ["rank","key","before",
            "after","column","from","to","end","start","global","like","min","max","point","time","date",
            "year","month","day","event","do","comment","commit","user","value","name","type","code","range","drop",
            "delete","length","exit","required","select","update","delete","insert","sql","database","driver","host",
            "password","url","port","show","schema","where","in","group","join","left","right","index","primary","having",
            "and","or","not","commit","grant","rollback","revoke","rename","drop","alter","create"];
			
var opTypes =[
{
	key:'SELECT',
	name:'查询列表',
	type:'GET'
},
{
	key:'SELECT_ONE',
	name:'查询单个',
	type:'GET'
},
{
	key:'INSERT',
	name:'新增',
	type:'POST'
},
{
	key:'UPDATE',
	name:'修改',
	type:'PUT'
},
{
	key:'DELETE',
	name:'删除',
	type:'DELETE'
}
];


var SQL_LIKE_NAME_ARRAY = ["name","title","type","phone","identityCard","identityId"];