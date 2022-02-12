<template>
  <div class="modal fade" tabindex="-1" ref="confirmed" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">

      <div class="alert alert-danger d-flex align-items-center" role="alert">
        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>
        <div>{{message}}</div>
      </div>
    </div>
  </div>
</template>

<script>

import { Modal } from "bootstrap";
function toggle(){
  if(!this.myModal)
    this.myModal = new Modal(this.$refs.confirmed)
  this.myModal.show();
  const supModal = this.myModal;
  setTimeout(()=> supModal.hide(),3000);
}
export default {
  name: "ErrorShower",
  props:{
    message:{
      type: String,
      default:"Errore inatteso"
    }
  },
  methods:{
    toggle
  },
  data: function (){
    return {
      myModal:undefined
    }
  }
}
</script>

<style scoped>

</style>