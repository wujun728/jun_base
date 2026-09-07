<template>
  <div ref="commandRef" class="w-full h-100"></div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref, watch } from 'vue';
  import { useECharts } from '@/hooks/web/useECharts';
  import { propTypes } from '@/utils/propTypes';
  import type { EChartsOption } from 'echarts';

  export default defineComponent({
    name: 'CommandChart',
    props: {
      data: propTypes.array.def([]),
    },
    setup(props) {
      const commandRef = ref<HTMLDivElement>();

      const { setOptions } = useECharts(commandRef as any);

      watch(
        () => props.data,
        () => {
          if (!commandRef.value) return;
          setEchartsOption(props.data);
        },
        { immediate: true },
      );

      onMounted(() => {
        setEchartsOption(props.data);
      });

      function setEchartsOption(data: any[]) {
        const option: EChartsOption = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)',
          },
          series: [
            {
              name: '命令',
              type: 'pie',
              roseType: 'radius',
              radius: [15, 95],
              center: ['50%', '38%'],
              data,
              animationEasing: 'cubicInOut',
              animationDuration: 1000,
            },
          ],
        };
        setOptions(option);
      }

      return {
        commandRef,
      };
    },
  });
</script>

<style scoped></style>
