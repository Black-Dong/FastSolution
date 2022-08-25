<template>
  <baidu-map center="呼和浩特" class="bm-view" ak="apKMRa4cdU2r1EelWCCH4bVrGTlzNeIl"
             :scroll-wheel-zoom="true" @click="clickBMap"
  >
    <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
    <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
    <bm-marker :position="{lng: 116.404, lat: 39.915}" :dragging="true" animation="BMAP_ANIMATION_BOUNCE"
               :icon="{url:fixPic, size: {width: 48, height: 48}}" @click="clickBMarker"
    ></bm-marker>
  </baidu-map>
</template>

<script>
import location from '@/assets/rubbish/垃圾桶_48.png'
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import { BmGeolocation, BmCityList, BmMarker } from 'vue-baidu-map'

export default {
  name: 'Index',
  components: {
    BaiduMap, BmGeolocation, BmCityList, BmMarker
  },
  data() {
    return {
      fixPic: location
    }
  },
  mounted() {
  },
  methods: {
    clickBMap(point) {
      // console.log(point)
      console.log(1)
      console.log(point)
    },
    clickBMarker(point){
      console.log(2)
      point.domEvent.stopPropagation()
      console.log(point)
    }
  }
}
</script>

<style scoped lang="scss">
.bm-view {
  width: 100%;
  height: 88vh;
}
</style>

