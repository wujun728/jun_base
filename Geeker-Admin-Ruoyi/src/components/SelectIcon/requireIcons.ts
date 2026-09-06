let icons: any[] = [];
const modules = import.meta.glob("./../../assets/icons/*.svg");
for (const path in modules) {
  const p = path.split("assets/icons/")[1]?.split(".svg")[0];
  icons.push(p);
}
console.log(icons);
export default icons;
