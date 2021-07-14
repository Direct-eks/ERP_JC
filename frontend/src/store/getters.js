const getters = {
    currentUser(state) {
        if (state.currentUser == null) {
            return sessionStorage.getItem('userName') != null ?
                sessionStorage.getItem('userName') : ''
        }
        return state.currentUser
    },
    currentUserRole(state) {
        if (state.currentUserRole == null) {
            return sessionStorage.getItem('userRole') != null ?
                sessionStorage.getItem('userRole') : ''
        }
        return state.currentUserRole
    },
    currentUserPermissions(state) {
        if (state.currentUserPermissions.length === 0) {
            return sessionStorage.getItem('userPermissions') != null ?
                JSON.parse(sessionStorage.getItem('userPermissions')) : []
        }
        return state.currentUserPermissions
    },
    currentUserPermittedRoundingAmount(state) {
        if (state.permittedRoundingAmount === '') {
            return sessionStorage.getItem('userPermissions') != null ?
                JSON.parse(sessionStorage.getItem('permittedRoundingAmount')) : '0'
        }
        return state.permittedRoundingAmount
    },
    currentUserIsPermitted(state) {
        return (permission) => {
            if (state.currentUserPermissions.length === 0) {
                const allPermissions = sessionStorage.getItem('userPermissions') != null ?
                    JSON.parse(sessionStorage.getItem('userPermissions')) : []
                return allPermissions.includes(permission)
            }
            return state.currentUserPermissions.includes(permission)
        }
    },

    /*----------- company data ------------*/
    companies(state) {
        return (id) => {
            return state.companies.has(id) ? state.companies.get(id) : null
        }
    },
    /* ------- relative company data -------*/
    relevantCompanies(state) {
        return (id) => {
            return state.relevantCompanies.has(id) ? state.relevantCompanies.get(id) : null
        }
    },
    /*----------- model data ------------*/
    models(state) {
        return (id) => {
            return state.models.has(id) ? state.models.get(id) : null
        }
    },
}

export default getters
