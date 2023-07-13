import React from 'react';
import { useEffect, useState } from 'react';
import { Spin, Layout, Form, Input, Space, message, Button } from 'antd';

const { Header, Footer, Content } = Layout;

const MyPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = (value: object) => {
    console.log(value);
  };

  const onFinishFailed = (value: object) => {
    console.log(value);
  };

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
            <Form
              name="basic"
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Username"
                name="username"
                rules={[{ required: true, message: 'Please input your username!' }]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: 'Please input your password!' }]}
              >
                <Input.Password />
              </Form.Item>

              <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                <Button type="primary" htmlType="submit">
                  Submit
                </Button>
              </Form.Item>
            </Form>
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

export default MyPage;