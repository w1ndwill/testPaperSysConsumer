// import { debounce } from './index';
export default {
  data() {
    return {
      myChart: null,
      resizeHandler: null
    };
  },
  computed: {
    open() {
        // return this.$store.state.setting.open;
        return true
    }
  },
  mounted() {
    this.resizeHandler = this.debounce(() => {
      if (this.myChart) {
        this.myChart.resize();
      }
    }, 100);
    this.initResizeEvent();
  },

  methods: {
    //监听resize
    initResizeEvent() {
      window.addEventListener('resize', this.resizeHandler);
    },
    //移除resize
    destroyResizeEvent() {
      window.removeEventListener('resize', this.resizeHandler);
    },
    /**
 * 函数防抖
 * @param {Function} func
 * @param {number} delay
 * @param {boolean} immediate
 * @return {*}
 */

 debounce(func, delay, immediate = false) {
  let timer,
    context = this;
  return (...args) => {
    if (immediate) {
      func.apply(context, args);
      immediate = false;
      return;
    }
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(context, args);
    }, delay);
  };
}

  },

  beforeDestroy() {
    this.destroyResizeEvent();
    if (!this.myChart) {
      return;
    }
    this.myChart.dispose();
    this.myChart.off('click');
    this.myChart = null;
  },

  activated() {
    this.initResizeEvent();
    if (this.myChart) {
      this.myChart.resize();
    }
  },

  deactivated() {
    this.destroyResizeEvent();
  },
  watch: {
    open() {
      if (this.myChart) {
        this.myChart.resize();
      }
    }
  }
};
