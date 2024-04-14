export default {
    Required: [
        { required: true, message: '必填', trigger: 'change' }
    ],
    userName: [
        { required: true, message: '请输入用户名', trigger: 'change' }
    ],
    pleaseSelect: [
        { required: true, message: '必选', trigger: 'change' }
    ],
    password: [
        { required: true, message: "密码不能不为空!!", trigger: 'change' },
        { pattern: /^(\w){6,20}$/, message: '只能输入6-20个字母、数字、下划线', trigger: 'change' }
    ],
    reSetPassword: [
        { pattern: /^(\w){6,20}$/, message: '只能输入6-20个字母、数字、下划线', trigger: 'change' }
    ],
    phone: [
        { required: true, pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号', trigger: 'change' }
    ],
    oldPassword: [
        { required: true, message: '请输入旧密码', trigger: 'change' }
    ],
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'change' },
        { pattern: /^(\w){6,20}$/, message: '只能输入6-20个字母、数字、下划线', trigger: 'change' }
    ],
    VerificationCode: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { min: 3, max: 3, message: '验证码长度为3', trigger: 'change' }
    ],
    photo: [
        { required: true, message: '请选择头像', trigger: 'blur' },
    ],
    email: [
        { pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/, message: '请输入正确的邮箱', trigger: 'change' }
    ],
    IdCard:[
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入身份证号正确', trigger: 'blur' }
    ],
    age: [
        { required: true, message: '必填', trigger: 'change' },
        { pattern:/^(?:[1-9]?\d|150)$/, message: '年龄范围0-150', trigger: 'change' }
    ]
}
