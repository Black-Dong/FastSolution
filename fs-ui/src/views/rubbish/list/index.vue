<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="categoryName"
            placeholder="请输入垃圾分类名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="categoryOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            node-key="categoryTree"
            ref="tree"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="垃圾名称" prop="rubbishName">
            <el-input
              v-model="queryParams.rubbishName"
              placeholder="请输入垃圾名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['rubbish:list:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['rubbish:list:edit']"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['rubbish:list:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['rubbish:list:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="listList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="垃圾名称" align="center" prop="rubbishName"/>
          <el-table-column label="垃圾分类名称" align="center" prop="categoryName"/>
          <el-table-column label="垃圾图片" align="center" prop="rubbishUrl" width="100">
            <template slot-scope="scope">
              <image-preview :src="String(scope.row.rubbishUrl)" :width="50" :height="50"/>
            </template>
          </el-table-column>
          <el-table-column label="处置方式" align="center" prop="disposalWay" show-overflow-tooltip/>
          <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['rubbish:list:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['rubbish:list:remove']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改垃圾管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="垃圾名称" prop="rubbishName">
          <el-input v-model="form.rubbishName" placeholder="请输入垃圾名称"/>
        </el-form-item>
        <el-form-item label="垃圾分类" prop="categoryId">
          <treeselect v-model="form.categoryId" :options="categoryOptions"
                      :normalizer="normalizer" placeholder="选择垃圾分类" @select="categorySelected"/>
        </el-form-item>
        <el-form-item label="垃圾图片">
          <image-upload v-model="form.rubbishUrl" :limit="1"/>
        </el-form-item>
        <el-form-item label="处置方式" prop="disposalWay">
          <el-input v-model="form.disposalWay" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listList, getList, delList, addList, updateList} from "@/api/rubbish/list";
import {listCategory} from "@/api/rubbish/category";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "List",
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 垃圾管理表格数据
      listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 垃圾分类名称
      categoryName: '',
      // 垃圾分类树选项
      categoryOptions: [],
      defaultProps: {
        children: "children",
        label: "categoryName"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rubbishName: null,
        categoryId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        rubbishName: [
          {required: true, message: "垃圾名称不能为空", trigger: "blur"}
        ],
        categoryId: [
          {required: true, message: "垃圾分类id不能为空", trigger: "blur"}
        ],
      }
    };
  },
  watch: {
    categoryName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getTreeselect();
  },
  methods: {
    categorySelected(category) {
      this.form.categoryName = category.categoryName
    },
    /** 转换垃圾分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.categoryId,
        label: node.categoryName,
        children: node.children
      };
    },
    /** 查询垃圾分类下拉树结构 */
    getTreeselect() {
      listCategory().then(response => {
        this.categoryOptions = this.handleTree(response.data, "categoryId");
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.categoryName.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.categoryId = data.categoryId;
      this.handleQuery();
    },
    /** 查询垃圾管理列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        listId: null,
        rubbishUrl: null,
        rubbishName: null,
        categoryId: null,
        categoryName: null,
        disposalWay: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$refs.tree.setCurrentKey(null)
      this.queryParams.categoryId = ''
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.listId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加垃圾管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const listId = row.listId || this.ids
      getList(listId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改垃圾管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.listId != null) {
            updateList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const listIds = row.listId || this.ids;
      this.$modal.confirm('是否确认删除垃圾管理编号为"' + listIds + '"的数据项？').then(function () {
        return delList(listIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rubbish/list/export', {
        ...this.queryParams
      }, `list_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
