package com.ruoyi.generator.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.jupiter.api.Test;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.GenTableColumn;

class VelocityUtilsTest
{
    @Test
    void artDesignProCrudUsesArtTemplates()
    {
        List<String> templates = VelocityUtils.getTemplateList(table("crud", VelocityUtils.ART_DESIGN_PRO, true));

        assertTrue(templates.contains("vm/artpro/api.ts.vm"));
        assertTrue(templates.contains("vm/vue/artpro/index.vue.vm"));
        assertTrue(templates.contains("vm/vue/artpro/view.vue.vm"));
        assertFalse(templates.contains("vm/ts/type.ts.vm"));
    }

    @Test
    void artDesignProTreeAndSubUseMatchingViews()
    {
        assertTrue(VelocityUtils.getTemplateList(table("tree", VelocityUtils.ART_DESIGN_PRO, false))
                .contains("vm/vue/artpro/index-tree.vue.vm"));
        assertTrue(VelocityUtils.getTemplateList(table("sub", VelocityUtils.ART_DESIGN_PRO, false))
                .contains("vm/vue/artpro/index.vue.vm"));
    }

    @Test
    void elementPlusTypeScriptAliasesRemainCompatible()
    {
        List<String> shortAlias = VelocityUtils.getTemplateList(table("crud", "element-plus-ts", false));
        List<String> longAlias = VelocityUtils.getTemplateList(table("crud", "element-plus-typescript", false));

        assertTrue(shortAlias.contains("vm/vue/v3ts/index.vue.vm"));
        assertTrue(shortAlias.contains("vm/ts/api.ts.vm"));
        assertTrue(longAlias.contains("vm/vue/v3ts/index.vue.vm"));
        assertTrue(longAlias.contains("vm/ts/type.ts.vm"));
    }

    @Test
    void legacyTemplatesRemainAvailable()
    {
        assertTrue(VelocityUtils.getTemplateList(table("crud", "element-ui", false))
                .contains("vm/vue/index.vue.vm"));
        assertTrue(VelocityUtils.getTemplateList(table("crud", "element-plus", false))
                .contains("vm/vue/v3/index.vue.vm"));
    }

    @Test
    void artDesignProTemplateSourcesCanBeLoaded()
    {
        VelocityInitializer.initVelocity();

        Velocity.getTemplate("vm/artpro/api.ts.vm");
        Velocity.getTemplate("vm/vue/artpro/index.vue.vm");
        Velocity.getTemplate("vm/vue/artpro/index-tree.vue.vm");
        Velocity.getTemplate("vm/vue/artpro/view.vue.vm");
    }

    @Test
    void artDesignProCrudTreeAndSubTemplatesCanBeRendered()
    {
        assertArtView(render(page("crud", "{}"), "vm/vue/artpro/index.vue.vm"));
        assertArtView(render(page("tree",
                "{\"treeCode\":\"node_id\",\"treeParentCode\":\"parent_id\",\"treeName\":\"node_name\"}"),
                "vm/vue/artpro/index-tree.vue.vm"));

        GenTable subPage = page("sub", "{}");
        GenTable subTable = page("crud", "{}");
        subTable.setClassName("DemoItem");
        subTable.setBusinessName("demoItem");
        subTable.setFunctionName("演示明细");
        subPage.setSubTableName("demo_item");
        subPage.setSubTableFkName("demo_id");
        subPage.setSubTable(subTable);
        assertArtView(render(subPage, "vm/vue/artpro/index.vue.vm"));
    }

    private void assertArtView(String content)
    {
        assertTrue(content.contains("ArtSearchBar"));
        assertTrue(content.contains("ArtTableHeader"));
        assertTrue(content.contains("v-auth"));
        assertFalse(content.contains("v-hasPermi"));
        assertFalse(content.contains("app-container"));
        assertFalse(content.contains("@/utils/request"));
    }

    private String render(GenTable table, String templatePath)
    {
        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);
        Template template = Velocity.getTemplate(templatePath);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    private GenTable page(String category, String options)
    {
        GenTable table = table(category, VelocityUtils.ART_DESIGN_PRO, false);
        table.setOptions(options);
        table.setClassName("Demo");
        table.setModuleName("system");
        table.setBusinessName("demo");
        table.setPackageName("com.ruoyi.system");
        table.setFunctionName("演示");
        table.setFormColNum(2);

        GenTableColumn id = column("demo_id", "demoId", "编号", "Long", "input");
        id.setIsPk("1");
        GenTableColumn name = column("demo_name", "demoName", "名称", "String", "input");
        name.setIsInsert("1");
        name.setIsEdit("1");
        name.setIsList("1");
        name.setIsQuery("1");
        name.setIsRequired("1");
        table.setColumns(Arrays.asList(id, name));
        table.setPkColumn(id);
        return table;
    }

    private GenTableColumn column(String columnName, String javaField, String comment, String javaType, String htmlType)
    {
        GenTableColumn column = new GenTableColumn();
        column.setColumnName(columnName);
        column.setJavaField(javaField);
        column.setColumnComment(comment);
        column.setJavaType(javaType);
        column.setHtmlType(htmlType);
        return column;
    }

    private GenTable table(String category, String webType, boolean view)
    {
        GenTable table = new GenTable();
        table.setTplCategory(category);
        table.setTplWebType(webType);
        table.setOptions("{\"genView\":" + view + "}");
        return table;
    }
}
