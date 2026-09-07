<template>
  <div ref="memoryRef" class="w-full h-100"></div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref, watch } from 'vue';
  import { useECharts } from '@/hooks/web/useECharts';
  import { propTypes } from '@/utils/propTypes';
  import type { EChartsOption } from 'echarts';

  export default defineComponent({
    name: 'MemoryChart',
    props: {
      data: propTypes.string.def('0'),
    },
    setup(props) {
      const memoryRef = ref<HTMLDivElement>();

      const { setOptions } = useECharts(memoryRef as any);

      watch(
        () => props.data,
        () => {
          if (!memoryRef.value) return;
          setEchartsOption(props.data);
        },
        { immediate: true },
      );

      onMounted(() => {
        setEchartsOption(props.data);
      });

      function getNearestPowerOfTen(num: number) {
        let power = 10;
        while (power <= num) {
          power *= 10;
        }
        return power;
      }

      function setEchartsOption(value: string) {
        // x10
        const formattedValue = Math.floor(parseFloat(value));
        // 最大值 10以内取10  100以内取100 以此类推
        const max = getNearestPowerOfTen(formattedValue);
        const options: EChartsOption = {
          tooltip: {
            formatter: '{b} <br/>{a} : ' + value + 'M',
          },
          series: [
            {
              animation: true,
              animationDuration: 1000,
              name: '峰值',
              type: 'gauge',
              min: 0,
              max,
              detail: {
                valueAnimation: true,
                formatter: value + 'M',
              },
              data: [
                {
                  value: parseFloat(value),
                  name: '内存消耗',
                },
              ],
            },
          ],
        };
        setOptions(options);
      }

      return {
        memoryRef,
      };
    },
  });
</script>

<style scoped></style>
