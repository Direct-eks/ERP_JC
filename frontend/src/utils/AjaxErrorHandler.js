import main from '~/main'

export default function AjaxErrorHandler(error) {
    console.log(error)
    if (error.response.status === 401) {
        sessionStorage.clear()
        setTimeout(() => {
            main.$router.replace('login').then(()=>{}).catch(()=>{})
        }, 3000)
    }
}
