const title = localStorage.getItem('singleAdminSiteName') || 'SCRM Java';

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`;
  }
  return `${title}`;
}
