import translations from './translationsGerman';
export default function customTranslate(template, replacements) {
  replacements = replacements || {};
  template = translations[template] || template;
  return template.replace(/{([^}]+)}/g, function(_, key) {
	 var str = replacements[key];
	  if(translations[replacements[key]] != null && translations [replacements[key]] != 'undefined'){
		  str = translations[replacements[key]];
	  }
    return  str || '{' + key + '}';
  });
}