import Docxtemplater from 'docxtemplater';
import PizZip from 'pizzip';
import JSZipUtils from 'jszip-utils';
import { saveAs } from 'file-saver';
export default {
    data() {
        return {
            serverFileName: '',
            boolList:[],
            choiceList:[],
            simpleList:[],
            exportFileName:[],
            testPaperName:''
        };
    },
    methods: {
        // 点击导出word
        exportWord() {
            const that = this;
            // 读取并获得模板文件的二进制内容
            if(typeof that.serverFileName === 'undefined'){
                that.$message("导出模板丢失~~,无法导出")
                return
            }
            JSZipUtils.getBinaryContent(that.serverFileName, function (error, content) {
                // serverFileName是模板。我们在导出的时候，会根据此模板来导出对应的数据
                // 抛出异常
                if (error) {
                    throw error;
                }
                // 创建一个PizZip实例，内容为模板的内容
                const zip = new PizZip(content);

                // 创建并加载docxtemplater实例对象
                const doc = new Docxtemplater().loadZip(zip);

                // 设置模板变量的值
                doc.setData({
                    // name: that.lookDetail.name,
                    // order_date: that.lookDetail.order_time
                    testPaperName:that.testPaperName||'',
                    boolList: that.boolList,
                    choiceList:that.choiceList,
                    simpleList:that.simpleList
                });
                try {
                    // 用模板变量的值替换所有模板变量
                    doc.render();
                } catch (error) {
                    // 抛出异常
                    const e = {
                        message: error.message,
                        name: error.name,
                        stack: error.stack,
                        properties: error.properties
                    };
                    console.log(JSON.stringify({ error: e }));
                    throw error;
                }

                // 生成一个代表docxtemplater对象的zip文件（不是一个真实的文件，而是在内存中的表示）
                const out = doc.getZip().generate({
                    type: 'blob',
                    mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
                });
                // 将目标文件对象保存为目标类型的文件，并命名
                saveAs(out, that.exportFileName||'');
            });
        }
    }
};

