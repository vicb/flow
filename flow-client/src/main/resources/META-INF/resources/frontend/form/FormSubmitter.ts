/* tslint:disable:max-classes-per-file */

export class FormSubmitter<T>{
    constructor(public formElementName: string,
        public endpointMethod: (value: T) => Promise<T|void>,
        public onDataSavedLocally?: () => void,
        public onDataSyncSucess?: () => void,
        public onDataSyncFail?: () => void){
    }

    async submit(value: T){
        await this.endpointMethod(value);
    }
}

export class OfflineFormSubmitter<T> extends FormSubmitter<T>{
    constructor(public formElementName: string,
        public endpointMethod: (value: T) => Promise<T|void>,
        public onDataSavedLocally?: () => void,
        public onDataSyncSucess?: () => void,
        public onDataSyncFail?: () => void){
            super(formElementName, endpointMethod, onDataSavedLocally, onDataSyncSucess, onDataSyncFail)
    }

    async submit(value: T){
        if(navigator.onLine){
            super.submit(value);
        }else{
            const formItem = {
                form: this.formElementName,
                value
            };
            let offlineForms = {};
            if(localStorage.getItem("offlineForms")!==null){
                offlineForms = JSON.parse(localStorage.getItem("offlineForms")!);
            }
            const id = Date.now();
            (offlineForms as any)[id]=formItem;
            localStorage.setItem("offlineForms", JSON.stringify(offlineForms));
            // tslint:disable-next-line: no-unused-expression
            this.onDataSavedLocally && this.onDataSavedLocally();
    
            window.addEventListener("online", async _ => {
                if(localStorage.getItem("offlineForms")!=null){
                    const data = JSON.parse(localStorage.getItem("offlineForms")!)[id].value;                
                    try{
                        await super.submit(data);
                        delete JSON.parse(localStorage.getItem("offlineForms")!)[id];
                        localStorage.setItem("offlineForms", JSON.stringify(offlineForms));

                        // tslint:disable-next-line: no-unused-expression
                        this.onDataSyncSucess && this.onDataSyncSucess();
                    }catch (e){
                        // tslint:disable-next-line: no-unused-expression
                        this.onDataSyncFail && this.onDataSyncFail();
                    }
                }
            });
        }
    }
}