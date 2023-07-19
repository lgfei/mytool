import axios from 'axios';

const api = "https://tool.lgfei.com/api";
class Service {
  constructor() {
  }

  public toHtml(md: string): Promise<any> {
    return axios.post(`${api}/v1/md/toHtml`, {
      md: md
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }
}

const service = new Service();

export default service;