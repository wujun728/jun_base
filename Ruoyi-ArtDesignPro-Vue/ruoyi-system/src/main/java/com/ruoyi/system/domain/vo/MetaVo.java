package com.ruoyi.system.domain.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.utils.StringUtils;

/**
 * 路由显示信息
 * 
 * @author ruoyi
 */
public class MetaVo
{
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    /**
     * 是否在后台内容区使用 iframe 打开
     */
    private boolean isIframe;

    /**
     * 页面按钮权限
     */
    private List<AuthVo> authList;

    private boolean showBadge;
    private String showTextBadge;
    private boolean isHideTab;
    private boolean fixedTab;
    private String activePath;
    private boolean isFullPage;
    private boolean newTab;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public MetaVo(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.isIframe = StringUtils.ishttp(link);
    }

    public MetaVo(String title, String icon, boolean noCache, String link)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link))
        {
            this.link = link;
        }
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    @JsonProperty("isIframe")
    public boolean isIframe()
    {
        return isIframe;
    }

    public void setIframe(boolean isIframe)
    {
        this.isIframe = isIframe;
    }

    public List<AuthVo> getAuthList()
    {
        return authList;
    }

    public void setAuthList(List<AuthVo> authList)
    {
        this.authList = authList;
    }

    public boolean isShowBadge() { return showBadge; }
    public void setShowBadge(boolean showBadge) { this.showBadge = showBadge; }
    public String getShowTextBadge() { return showTextBadge; }
    public void setShowTextBadge(String showTextBadge) { this.showTextBadge = showTextBadge; }
    @JsonProperty("isHideTab")
    public boolean isHideTab() { return isHideTab; }
    public void setHideTab(boolean isHideTab) { this.isHideTab = isHideTab; }
    public boolean isFixedTab() { return fixedTab; }
    public void setFixedTab(boolean fixedTab) { this.fixedTab = fixedTab; }
    public String getActivePath() { return activePath; }
    public void setActivePath(String activePath) { this.activePath = activePath; }
    @JsonProperty("isFullPage")
    public boolean isFullPage() { return isFullPage; }
    public void setFullPage(boolean isFullPage) { this.isFullPage = isFullPage; }
    public boolean isNewTab() { return newTab; }
    public void setNewTab(boolean newTab) { this.newTab = newTab; }
}
