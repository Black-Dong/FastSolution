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

      <el-row class="m-t-20" :gutter="20" type="flex" justify="center">
        <el-col :span="24" style="text-align: center"
                v-if="!hasRubbishResult && queryParams.rubbishName != ''">
          {{ resultMessage }}
        </el-col>
        <el-col v-if="JSON.stringify(rubbishResult) !== '{}'" :span="6" style="text-align: center">
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
        <el-col v-if="JSON.stringify(rubbishResult) !== '{}' && rubbishResult.disposalWay !==null" :span="6">
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

    <baidu-map center="北京" class="bm-view m-t-20"
               ak="apKMRa4cdU2r1EelWCCH4bVrGTlzNeIl"
               :zoom="30"
               :scroll-wheel-zoom="true" @click="clickBMap"
    >
      <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
      <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <bm-marker v-for="item in bucketList" :key="item.bucketId"
                 :position="{lng: item.lng, lat: item.lat}" :dragging="false" animation="BMAP_ANIMATION_BOUNCE"
                 :icon="{url:fixPic, size: {width: 48, height: 48}}"
                 :data-bucket-id="item.id"
      >
      </bm-marker>
    </baidu-map>
  </div>
</template>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import {BmGeolocation, BmCityList, BmMarker,BmNavigation} from 'vue-baidu-map'

import {selectByRubbishName} from "@/api/rubbish/list";
import {parseStrEmpty} from "@/utils/ruoyi";
import {listBucket} from "@/api/rubbish/bucket";

export default {
  name: 'Index',
  components: {
    BaiduMap, BmGeolocation, BmCityList, BmMarker,BmNavigation
  },
  data() {
    return {
      bucketList: [],
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
    this.getList()
  },
  methods: {
    getList() {
      console.log(1)
      listBucket().then(response => {
        this.bucketList = response.rows
      })
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
    },
    clickBMarker(point) {
      // console.log(point)
      point.domEvent.stopPropagation()
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

