import { ReactNode, useEffect, useState } from 'react';
import { Spin, Layout, Input, message, Space } from 'antd';
import './App.css';

const { Header, Footer, Content } = Layout;
const { Search } = Input;

interface PropsType{
  appContent?: ReactNode;
}

const App: React.FC<PropsType> = ({appContent}) => {
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  return (
    <Spin tip="Loading" size="large" spinning={loading}>
      <div className="App">
        {contextHolder}
        <Layout>
          <Header className='App-header'>
            Welcome to tool.lgfei.com!
          </Header>
          <Content className='App-content'>
            <Space direction={"vertical"} style={{width:"80%"}}>
              {appContent}
            </Space>
          </Content>
          <Footer className='App-footer'>
            <Space>关于我们 | 免责声明 | Copyright © 2018-2024 All Rights Reserved. | 粤ICP备18059979号-1</Space>  
          </Footer>
        </Layout>
      </div>
    </Spin>
  ); 
};

export default App;