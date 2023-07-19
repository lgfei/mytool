import React from 'react';
import { useEffect, useState } from 'react';
import { message, Card, Button, Form, Input, Space, } from 'antd';

import service from "./service";

const { TextArea } = Input;

const MyPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = (value: object) => {
    console.log(value);
  };

  const onFinishFailed = (value: object) => {
    console.log(value);
  };

  const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    console.log('Change:', e.target.value);
  };

  const toHtml = (md: string) => {
    service.toHtml(md);
  }

  return (
    <>
      <Card title="Markdown语法解析" bordered={false}>
        <Form
          name="basic"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            label=""
            name="md"
            rules={[{ required: true, message: '请输入md内容' }]}
          >
            <TextArea
              showCount
              maxLength={1048576}
              style={{ height: 260, resize: 'none' }}
              onChange={onChange}
              placeholder="在此输入markdown内容或者http链接地址"
            />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit" onClick={(e) => toHtml}>
                toHtml
              </Button>
              <Button type="primary" htmlType="submit">
                toDocx
              </Button>
              <Button type="primary" htmlType="submit">
                toPdf
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </>
  );
};

export default MyPage;