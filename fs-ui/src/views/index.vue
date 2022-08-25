<template>
  <div>
    <div class="container">
      <el-row class="m-t-20">
        <el-col class="col-md-12 el-col-xs-12" style="text-align: center;">
          <img :src="bucketPic" width="50px"
               style="display: inline;vertical-align: middle;" alt="图片加载异常">
          <h1 style="display: inline;vertical-align: middle">垃圾分类查询</h1>
        </el-col>
      </el-row>
      <el-row class="m-t-20">
        <el-col class="col-md-12 el-col-xs-12" style="text-align: center;">
          <el-input v-model="queryParams.rubbishName" placeholder=""
                    type="text" style="width: 500px" @keyup.native.enter="subsearch">
            <template v-slot:append>
              <el-button class="btn btn-primary" type="submit" @click="subsearch"
                         icon="el-icon-search">查询
              </el-button>
            </template>
          </el-input>
        </el-col>
      </el-row>

      <el-row class="m-t-20" :gutter="20">
        <el-col :span="24" style="text-align: center"
                v-if="!hasRubbishResult && queryParams.rubbishName != ''">
          {{ resultMessage }}
        </el-col>
        <el-col v-if="JSON.stringify(rubbishResult) !== '{}'" :offset="6" :span="6" style="text-align: center">
          <el-card :body-style="{ padding: '0px' }">
            <image-preview :src="String(rubbishResult.rubbishUrl)" width="100%" height="200px"/>
            <div style="padding: 14px 14px 10px 14px;">
              <span>{{ rubbishResult.rubbishName }}</span>
              <div class="bottom">
                <span class="time">{{ rubbishResult.categoryName }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col v-if="JSON.stringify(rubbishResult) !== '{}'" :span="6">
          <el-card :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix" style="text-align: center">
              <span>建议处理方式</span>
            </div>
            <div style="margin: 10px; height: 212px" >
              {{ rubbishResult.disposalWay }}
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin: 20px 10px" :gutter="20">
        <el-col :span="6" style="text-align: center;">
          <image-preview :src="categoryPic.n1" :is-local="true"></image-preview>
        </el-col>
        <el-col :span="6" style="text-align: center;">
          <image-preview :src="categoryPic.n2" :is-local="true"></image-preview>
        </el-col>
        <el-col :span="6" style="text-align: center;">
          <image-preview :src="categoryPic.n3" :is-local="true"></image-preview>
        </el-col>
        <el-col :span="6" style="text-align: center;">
          <image-preview :src="categoryPic.n4" :is-local="true"></image-preview>
        </el-col>
      </el-row>
    </div>

    <baidu-map center="呼和浩特" class="bm-view m-t-20"
               ak="apKMRa4cdU2r1EelWCCH4bVrGTlzNeIl"
               @click="clickBMap"
    >
      <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
      <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <bm-marker :position="{lng: 116.404, lat: 39.915}" :dragging="true" animation="BMAP_ANIMATION_BOUNCE"
                 :icon="{url:fixPic, size: {width: 48, height: 48}}" @click="clickBMarker"
      ></bm-marker>
    </baidu-map>
  </div>
</template>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import {BmGeolocation, BmCityList, BmMarker,BmNavigation} from 'vue-baidu-map'

import {selectByRubbishName} from "@/api/rubbish/list";
import {parseStrEmpty} from "@/utils/ruoyi";


export default {
  name: 'Index',
  components: {
    BaiduMap, BmGeolocation, BmCityList, BmMarker,BmNavigation
  },
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rubbishName: '',
      },
      categoryPic: {
        n1: require('@/assets/rubbish/n1.png'),
        n2: require('@/assets/rubbish/n2.png'),
        n3: require('@/assets/rubbish/n3.png'),
        n4: require('@/assets/rubbish/n4.png'),
      },
      fixPic: require('@/assets/rubbish/垃圾桶_48.png'),
      bucketPic: require('@/assets/rubbish/垃圾分类查询.png'),
      rubbishResult: {},
      hasRubbishResult: true,
      nullResultMessage: '未查到完全匹配字段。'
    }
  },
  computed: {
    resultMessage() {
      return this.nullResultMessage + '\n是否查询其他互联网上相关垃圾: ' + this.queryParams.rubbishName
    }
  },
  watch: {
    'queryParams.rubbishName'() {
      this.hasRubbishResult = true
    }
  },
  mounted() {
  },
  methods: {
    // 切割公司列表
    splitCut(str) {
      let strS = name.split("\n")
      return strS
    },
    subsearch() {
      if (parseStrEmpty(this.queryParams.rubbishName) !== '') {
        this.rubbishResult = {}
        selectByRubbishName(this.queryParams.rubbishName).then(response => {
          if (parseStrEmpty(response.data) !== '') {
            this.hasRubbishResult = true
            this.rubbishResult = response.data
          } else {
            this.hasRubbishResult = false
          }
        })
      }
    },
    clickBMap(point) {
      // console.log(point)
      console.log(1)
      console.log(point)
    },
    clickBMarker(point) {
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
  height: 500px;
}

.m-t-20 {
  margin-top: 20px;
}

.time {
  font-size: 13px;
  color: #ff4949;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}
</style>

