export interface Position {
	x: number
	y: number
}
export interface GridRect extends Position{
	row : number
	// x : number
	// y : number
	x1 : number
	y1 : number
}
export interface Grid extends Position{
	id : string
	index : number
	oldindex : number
	content : any
	// x : number
	// y : number
	class : string
	show: boolean
}