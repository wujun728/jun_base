import { ref, reactive, computed } from 'vue';
import { onShow } from '@dcloudio/uni-app';

/**
 * 地址信息接口
 */
export interface AddressInfo {
    id: string;          // 地址ID
    name: string;        // 收货人姓名
    phone: string;       // 手机号码(已脱敏)
    region: string;      // 地区(如: 广东省深圳市南山区)
    address: string;     // 详细地址
    tag: string;         // 地址标签(如: 家、公司、学校)
    isDefault: boolean;  // 是否为默认地址
}

/**
 * 示例地址数据
 */
const sampleAddresses: AddressInfo[] = [
    {
        id: '1',
        name: '张三',
        phone: '13712348888',
        region: '广东省深圳市南山区',
        address: '科技园南路888号创新大厦A座10楼',
        tag: '公司',
        isDefault: true
    },
    {
        id: '2',
        name: '李四',
        phone: '13912345678',
        region: '广东省深圳市福田区',
        address: '福中路1000号海城大厦B座20楼2001室',
        tag: '家',
        isDefault: false
    },
    {
        id: '3',
        name: '王五',
        phone: '15812342233',
        region: '广东省广州市天河区',
        address: '天河路100号天河城购物中心附近小区A栋3单元701室',
        tag: '学校',
        isDefault: false
    }
];

// 共享的地址数据
const addressStore = {
    list: ref<AddressInfo[]>([]),
    tags: ref(['家', '公司', '学校'])
};

/**
 * 格式化手机号，中间4位用星号代替
 */
export function formatPhoneNumber(phone: string): string {
    return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
}

/**
 * 地址列表页面Hook
 * @description 提供地址列表页面所需的状态和方法
 */
export function useAddressListPage() {
    // 从共享store获取响应式数据
    const addressList = addressStore.list;

    // 使用计算属性计算是否为空状态
    const emptyStatus = computed(() => addressList.value.length === 0);

    // 更新地址列表
    function refreshAddressList() {
        addressList.value = sampleAddresses
        // 实际项目中，这里应该调用API获取最新的地址列表
        // const response = await api.getAddressList();
        // addressList.value = response.data;
    }

    // 设置默认地址
    function setDefaultAddress(id: string): boolean {
        const index = addressList.value.findIndex(item => item.id === id);

        if (index === -1) return false;

        // 更新所有地址的默认状态
        addressList.value = addressList.value.map(item => ({
            ...item,
            isDefault: item.id === id
        }));

        return true;
    }

    // 删除地址
    function deleteAddress(id: string): boolean {
        const initialLength = addressList.value.length;
        addressList.value = addressList.value.filter(item => item.id !== id);
        return addressList.value.length !== initialLength;
    }

    // 页面显示时刷新数据
    onShow(() => {
        refreshAddressList();
    });

    return {
        // 响应式状态
        addressList,
        emptyStatus,
        // 方法
        setDefaultAddress,
        deleteAddress,
        refreshAddressList
    };
}

/**
 * 地址编辑页面Hook
 * @description 提供地址编辑页面所需的状态和方法
 */
export function useAddressEditPage() {
    // 从共享store获取响应式数据
    const addressList = addressStore.list;
    const addressTags = addressStore.tags;

    // 页面状态
    const isEdit = ref(false);
    const editId = ref('');
    const defaultAddress = ref(false);
    const selectedTag = ref('家');

    // 表单数据
    const form = reactive({
        name: '',
        phone: '',
        region: '',
        address: ''
    });

    // 加载编辑数据
    function loadAddressData(id: string): boolean {
        const address = addressList.value.find(item => item.id === id);

        if (!address) return false;

        // 填充表单数据
        form.name = address.name;
        form.phone = address.phone;
        form.region = address.region;
        form.address = address.address;
        selectedTag.value = address.tag;
        defaultAddress.value = address.isDefault;

        return true;
    }

    // 初始化页面
    function initEditPage(id?: string) {
        // 重置状态
        isEdit.value = !!id;
        editId.value = id || '';
        defaultAddress.value = false;
        selectedTag.value = '家';

        form.name = '';
        form.phone = '';
        form.region = '';
        form.address = '';

        // 如果是编辑模式，加载地址数据
        if (id) {
            loadAddressData(id);
        }
    }

    // 保存地址
    function saveAddress(): boolean {
        if (isEdit.value) {
            // 编辑现有地址
            const index = addressList.value.findIndex(item => item.id === editId.value);
            if (index === -1) return false;

            // 如果设为默认，更新其他地址
            if (defaultAddress.value) {
                addressList.value = addressList.value.map(item => {
                    if (item.id !== editId.value) {
                        return { ...item, isDefault: false };
                    }
                    return item;
                });
            }

            // 更新当前地址
            addressList.value[index] = {
                ...addressList.value[index],
                name: form.name,
                phone: form.phone,
                region: form.region,
                address: form.address,
                tag: selectedTag.value,
                isDefault: defaultAddress.value
            };
        } else {
            // 添加新地址
            const newId = Date.now().toString();

            // 如果设为默认，更新其他地址
            if (defaultAddress.value) {
                addressList.value = addressList.value.map(item => ({
                    ...item,
                    isDefault: false
                }));
            }

            // 添加新地址
            addressList.value.push({
                id: newId,
                name: form.name,
                phone: form.phone,
                region: form.region,
                address: form.address,
                tag: selectedTag.value,
                isDefault: defaultAddress.value
            });
        }

        return true;
    }

    // 删除地址
    function deleteAddress(): boolean {
        if (!isEdit.value) return false;

        const initialLength = addressList.value.length;
        addressList.value = addressList.value.filter(item => item.id !== editId.value);

        return addressList.value.length !== initialLength;
    }

    return {
        // 响应式状态
        isEdit,
        editId,
        form,
        defaultAddress,
        selectedTag,
        addressTags,
        // 方法
        initEditPage,
        saveAddress,
        deleteAddress
    };
}