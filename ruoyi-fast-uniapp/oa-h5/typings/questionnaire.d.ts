interface QuestionnaireDesignInfo {
  id: number;
  crud_id: number;
  field_name_en: string;
  data_dict_id: number;
  options: Array<any>;
  association_crud_id: number;
  field_name: string;
  form_value: string;
  field_type: string;
  form_field_uniqid: string;
  is_default_value_not_null: number;
  create_modify: number;
  update_modify: number;
  is_uniqid: number;
  crud: {
    id: number;
    table_name_en: string;
  };
  association: any;
  data_id: number;
}
