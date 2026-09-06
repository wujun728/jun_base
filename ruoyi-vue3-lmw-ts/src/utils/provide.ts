import { useDict } from '@/utils/dict'
import hasPermi from '@/utils/hasPermi'
import { download } from '@/utils/hsj/service/index'
import { handleTree, selectDictLabel, selectDictLabels } from '@/utils/ruoyi'
import { formatSearchTime } from '@/utils/hsj/utils'
import { parseTime } from '@/utils/hsj/timeFormat'
import { tab, auth, modal, $download, isSmallScreen } from '@/plugins/index'
const proxy = {
  download,
  useDict,
  parseTime,
  handleTree,
  selectDictLabel,
  selectDictLabels,
  hasPermi,
  formatSearchTime,
  $tab: tab,
  $auth: auth,
  $modal: modal,
  $download: $download,
  $isSmallScreen: isSmallScreen(),
}
export { proxy }
