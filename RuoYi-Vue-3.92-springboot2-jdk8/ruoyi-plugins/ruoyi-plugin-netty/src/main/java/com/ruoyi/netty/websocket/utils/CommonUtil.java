package com.ruoyi.netty.websocket.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CommonUtil {

   /**
    * @param uri
    * @param uriTemplates
    * @return
    */
   public static int mathUri(String uri, String uriTemplate) {
      String[] uriSplit = uri.split("/");
      String[] tempalteSplit = uriTemplate.split("/");
      if (uriSplit.length != tempalteSplit.length) {
         return -1;
      }
      int mathLevel = 0;
      for (int index = 1; index < tempalteSplit.length; index++) {
         if (tempalteSplit[index].equals("?")) {
            mathLevel = mathLevel + index;
            continue;
         }
         if (!tempalteSplit[index].equals(uriSplit[index])) {
            return -1;
         } else {
            mathLevel = mathLevel + tempalteSplit.length + 1;
         }
      }
      return mathLevel;
   }

   public static Map<String, String> parseQueryParameters(String query) {
      if (query == null || query.isEmpty()) {
         return Map.of();
      }

      Map<String, String> params = new HashMap<>();
      String[] pairs = query.split("&");
      for (String pair : pairs) {
         String[] keyValue = pair.split("=");
         if (keyValue.length > 1) {
            params.put(keyValue[0], keyValue[1]);
         } else {
            params.put(keyValue[0], "");
         }
      }
      return params;
   }

   public static Map<String, String> parsePathParam(String uri, List<String> pathParams, String uriTemplate) {
      int index = 0;
      String[] split = uriTemplate.split("/");
      String[] split2 = uri.split("/");
      Map<String, String> map = new HashMap<>();
      for (int i = 1; i < split.length; i++) {
         if (split[i].equals("?")) {
            map.put(pathParams.get(index), split2[i]);
            index++;
         }
      }
      return map;
   }

   public static Integer getMax(Set<Integer> set) {
      Optional<Integer> maxNumber = set.stream().max(Integer::compare);
      if (maxNumber.isPresent()) {
         System.out.println("Max number: " + maxNumber.get());
      } else {
         System.out.println("The list is empty");
      }
      return maxNumber.get();
   }
}
