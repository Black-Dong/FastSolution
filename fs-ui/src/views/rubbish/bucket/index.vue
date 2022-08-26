<template>
  <div>
    <baidu-map center="北京" class="bm-view" ak="apKMRa4cdU2r1EelWCCH4bVrGTlzNeIl"
               :zoom="30"
               :scroll-wheel-zoom="true" @rightclick="clickBMap"
    >
      <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
                      :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <bm-marker v-for="item in bucketList" :key="item.bucketId"
                 :position="{lng: item.lng, lat: item.lat}" :dragging="false" animation="BMAP_ANIMATION_BOUNCE"
                 :icon="{url:fixPic, size: {width: 48, height: 48}}"
                 :data-bucket-id="item.id"
                 @click="clickBMarker($event, item)"
      >
      </bm-marker>
    </baidu-map>
    <el-drawer
      :title="drawerTitle"
      :visible.sync="drawer"
      :before-close="handleClose"
      ref="drawer"
      direction="rtl">
      <div style="display: flex; flex-direction: column;height: 100%;padding: 20px;">
        <el-form :model="bucketForm" ref="bucketForm">
          <el-form-item label="垃圾站名称" :label-width="formLabelWidth" prop="bucketName">
            <el-input v-model="bucketForm.bucketName" @input="inpChange($event)"></el-input>
          </el-form-item>
          <el-form-item label="经度" :label-width="formLabelWidth" prop="lng">
            <el-input v-model="bucketForm.lng" :readonly="isAdd"></el-input>
          </el-form-item>
          <el-form-item label="纬度" :label-width="formLabelWidth" prop="lat">
            <el-input v-model="bucketForm.lat" :readonly="isAdd"></el-input>
          </el-form-item>
        </el-form>
        <div style="display: flex;flex-direction: column">
          <div style="display: flex">
            <el-button @click="cancelForm" style="flex: 1">取 消</el-button>
            <el-button type="primary" style="flex: 1"
                       @click="$refs.drawer.closeDrawer()" :loading="loading">
              {{ loading ? '提交中 ...' : '确 定' }}
            </el-button>
          </div>
          <el-button v-if="!isAdd"
                     type="danger" style="flex: 1;margin-top: 8px"
                     @click="removeBucket">删 除
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import {BmGeolocation, BmCityList, BmMarker} from 'vue-baidu-map'
import {listBucket, getBucket, addBucket, updateBucket, delBucket} from "@/api/rubbish/bucket";


export default {
  name: 'Index',
  components: {
    BaiduMap, BmGeolocation, BmCityList, BmMarker
  },
  data() {
    return {
      bucketForm: {
        bucketId: null,
        bucketName: null,
        lng: null,
        lat: null
      },
      formLabelWidth: '100px',
      isAdd: true,
      loading: false,
      drawerTitle: '',
      drawer: false,
      fixPic: require('@/assets/rubbish/垃圾桶_48.png'),
      bucketList: []
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    inpChange(e) {
      this.$forceUpdate()
    },
    handleClose(done) {
      if (this.loading) {
        return;
      }
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.loading = true
          if (this.bucketForm.bucketId != null) {
            updateBucket(this.bucketForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.getList()
            })
          } else {
            addBucket(this.bucketForm).then(response => {
              this.$modal.msgSuccess("提交成功");
              this.getList()
            })
          }
          this.cancelForm()
        })
        .catch(_ => {
        });
    },
    removeBucket() {
      this.$confirm('确定要删除垃圾桶' + this.bucketForm.bucketName + '吗？')
        .then(_ => {
          delBucket(this.bucketForm.bucketId).then(response => {
            this.$modal.msgSuccess("删除成功")
            this.getList()
          })
          this.cancelForm()
        })
        .catch(_ => {
        });
    },

    cancelForm() {
      this.loading = false;
      this.drawer = false;
      this.bucketFormReset()
    },

    bucketFormReset(){
      if (this.$refs.bucketForm !== undefined){
        this.$refs.bucketForm.resetFields()
      }
    },

    clickBMap(point) {
      this.bucketFormReset()
      this.isAdd = true
      this.bucketForm.lng = point.point.lng
      this.bucketForm.lat = point.point.lat
      this.drawerTitle = '新增垃圾站'
      this.drawer = true
    },
    clickBMarker(point, bucket) {
      this.bucketFormReset()
      this.isAdd = false
      point.domEvent.stopPropagation()
      getBucket(bucket.bucketId).then(response => {
        console.log(response.data)
        this.bucketForm.lng = response.data.lng
        this.bucketForm.lat = response.data.lat
        this.bucketForm.bucketName = response.data.bucketName
        this.bucketForm.bucketId = response.data.bucketId
      })
      this.drawerTitle = '修改垃圾站'
      this.drawer = true
    },
    getList() {
      listBucket().then(response => {
        this.bucketList = response.rows
      })
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

