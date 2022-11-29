<template>
  <div class="wrapper">
    <div class="header">
      <mt-header title="Cooperation agreement">
        <router-link to="/user" slot="left">
          <mt-button icon="back">back</mt-button>
        </router-link>
        <!-- <mt-button icon="more" slot="right"></mt-button> -->
      </mt-header>
    </div>
    <div style="margin:12px 20px;">
      <h1>Entrusted Investment Cooperation Agreement</h1>
      <p></p>
      <p>This cooperation agreement (hereinafter referred to as "this agreement") is signed by the following parties:</p>
      <p>Party A</p>
      <p>Article 1 General</p>
      <p>
        1. Party A is a qualified institutional investor in stock index and commodity options stipulated by the China Financial Exchange, futures companies, futures risk subsidiaries, etc., and has the qualifications and authority to participate in transactions. Party B has independent knowledge and investment experience in financial market investment risks A natural person who has investment ability and anti-risk ability in the financial market, recognizes and understands the trading rules of CFFEX, futures companies, and futures risk subsidiaries, and entrusts Party A to participate in stock index and commodity options investment.</p>
    </div>
    <div class="agree-footer text-center">
      <div class="agree">
        <p @click="isAgree">
          <i :class="agree?'glyphicon glyphicon glyphicon-ok-sign red':'glyphicon glyphicon-ok-circle'"></i>
          I agree
          <a href="#">"Entrustment Cooperation Agreement"</a>
        </p>
      </div>
      <div class="btn-box">
        <mt-button :class="agree?'btn btn-red':'btn btn-default'" size="small" @click="toBuy()">Confirm</mt-button>
        <mt-button class="btn btn-cancel" size="small" @click="toBuy">Cancal</mt-button>
      </div>
    </div>
  </div>

</template>
<script>
export default {
  data () {
    return {
      agree: false
    }
  },
  methods: {
    isAgree () {
      let i = false
      let j = true
      this.agree = this.agree ? i : j
    },
    toBuy () {
      this.$router.push('/buy')
    }
  }
}
</script>
<style lang="less" scoped>
  h1 {
    font-size: 0.25rem;
    text-align: center;
  }

  h4 {
    font-size: 13px;
    line-height: 25px;
    margin: 25px 0;
  }

  p {
    font-size: 12px;
    line-height: 25px;
    text-indent: 2em;
  }

  .agree-footer {
    .agree {
      padding: 0.2rem 0;
    }

    a {
      color: #177be1;
    }

  }
</style>
