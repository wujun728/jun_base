import http from "@/api";

export function getEnum(enumType: string) {
  return () => http.get("/system/dict/data/type/" + enumType);
}
