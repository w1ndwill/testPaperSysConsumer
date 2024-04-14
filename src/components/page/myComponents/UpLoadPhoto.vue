<template>
    <div>
        <!--   action="http://localhost:9527/upload/photo" -->
        <el-upload
            class="avatar-uploader"
            drag
            accept="image/png,image/jpeg"
            ref="upload"
            name="file"
            :data="data|| {}"
            :show-file-list="false"
            :action="serverPath || ''"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :multiple="false"
        >
            <img v-if="photoSrc" :src="photoSrc" class="avatar"  />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <!-- <el-button type="primary" style="margin-left: 40px; margin-top: 10px" @click="submitUpload">上传到服务器</el-button> -->
    </div>
</template>
<script>
export default {
    props: {
        value:{
            type:String,
            require:false,
        },
        serverPath: String,
        data:Object
    },
    watch:{
        value(){
            this.photoSrc=this.value
        }
    },
    data() {
        return {
            photoSrc: this.value,
        };
    },
    methods: {
        handleAvatarSuccess(res, file) {
            this.$emit('success',res,file);
            this.$emit('input',res.imageUrl);
             setTimeout(() => {
                this.photoSrc = URL.createObjectURL(file.raw);
            }, 100);
        },
        beforeAvatarUpload(file) {
            if (!file) {
                file = this.file;
            }
            const isJPG = file.type === 'image/jpeg';
            const isPNG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG && !isPNG) {
                this.$message.error('上传头像只能图片[jpg/png]格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            let isUpload = (isJPG || isPNG) && isLt2M;
            if(!isUpload){
                this.$emit('error',res,file);
            }
            return this.isUpload;
        }
    }
};
</script>
<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    width: 178px;
    height: 178px;
    position: relative;
    overflow: hidden;
}
.avatar-uploader .el-upload:hover {
    border-color: #409eff;
}
.avatar-uploader img {
    width: 178px;
    height: 178px;
}
.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    margin-left: -50%;
    /* text-align: center; */
}
.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>