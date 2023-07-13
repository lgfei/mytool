import { useEffect, useState } from 'react';
import { Spin, Layout, Input, message, Card, Col, Row, Space } from 'antd';
import './App.css';

const { Header, Footer, Content } = Layout;
const { Search } = Input;

const App: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const onSearch = (value: string) => console.log(value);

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
              <Search placeholder="搜一搜" onSearch={onSearch} enterButton />
              <Row gutter={16}>
                <Col span={8}>
                  <Card className='block' title="Markdown语法解析" bordered={false}>
                    输入markdown内容或者文件地址，一键转化成你想要的文件格式
                    <br/>
                    <a href='md-converter' target='_blank'>learn more</a>
                  </Card>
                </Col>
                <Col span={8}>
                  <Card className='block' title="DDL解析" bordered={false}>
                    根据表结构自动生成POJO和JSON模拟数据，根据DDL生成回滚语句
                    <br/>
                    <a href='#'>learn more</a>
                  </Card>
                </Col>
                <Col span={8}>
                  <Card className='block' title="properties与yaml互转" bordered={false}>
                    properties与yaml互转
                    <br/>
                    <a href='#'>learn more</a>
                  </Card>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={8}>
                  <Card className='block' title="自动生成nginx配置文件" bordered={false}>
                    自动生成nginx配置文件
                    <br/>
                    <a href='#'>learn more</a>
                  </Card>
                </Col>
                <Col span={8}>
                  <Card className='block' title="加解密" bordered={false}>
                    加解密
                    <br/>
                    <a href='#'>learn more</a>
                  </Card>
                </Col>
                <Col span={8}>
                  <Card className='block' title="..." bordered={false}>
                    持续更新中...
                    <br/>
                  </Card>
                </Col>
              </Row>
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