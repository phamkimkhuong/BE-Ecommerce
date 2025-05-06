<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Đặt hàng thành công</title>
    <style type="text/css">
        /* Đảm bảo tương thích với các trình đọc email */
        body {
            margin: 0;
            padding: 0;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            font-size: 16px;
            line-height: 1.6;
            color: #333333;
            background-color: #f5f5f5;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
        }

        .header {
            background-color: #4caf50;
            padding: 20px;
            text-align: center;
        }

        .header h1 {
            color: #ffffff;
            margin: 0;
            font-size: 24px;
        }

        .content {
            padding: 20px;
        }

        .order-info {
            background-color: #f9f9f9;
            border: 1px solid #eeeeee;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }

        .order-info h2 {
            margin-top: 0;
            color: #4caf50;
            font-size: 18px;
        }

        .order-details {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .order-details th {
            background-color: #f2f2f2;
            text-align: left;
            padding: 10px;
            border-bottom: 1px solid #dddddd;
        }

        .order-details td {
            padding: 10px;
            border-bottom: 1px solid #dddddd;
        }

        .footer {
            background-color: #f2f2f2;
            padding: 15px;
            text-align: center;
            font-size: 14px;
            color: #666666;
        }

        .button {
            display: inline-block;
            background-color: #4caf50;
            color: #ffffff;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 5px;
            margin-top: 15px;
        }

        .total-row {
            font-weight: bold;
            background-color: #f9f9f9;
        }

        @media only screen and (max-width: 600px) {
            .container {
                width: 100% !important;
            }

            .content {
                padding: 10px !important;
            }

            .order-details th,
            .order-details td {
                padding: 8px !important;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Đặt hàng thành công</h1>
    </div>
    <div class="content">
        <p>Xin chào <strong>${customerName}</strong>,</p>
        <p>
            Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi. Đơn hàng của bạn đã
            được xác nhận và đang được xử lý.
        </p>

        <div class="order-info">
            <h2>Thông tin đơn hàng</h2>
            <p><strong>Mã đơn hàng:</strong> #${orderId}</p>
            <p><strong>Ngày đặt hàng:</strong> ${orderDate}</p>
            <p><strong>Phương thức thanh toán:</strong> ${paymentMethod}</p>
            <p>
                <strong>Trạng thái:</strong>
                <span style="color: #4caf50; font-weight: bold"
                >${orderStatus}</span
                >
            </p>
        </div>

        <h3>Chi tiết đơn hàng</h3>
        <table class="order-details">
            <thead>
            <tr>
                <th>Sản phẩm</th>
                <th>Số lượng</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
            <!-- Vòng lặp qua các sản phẩm -->
            ${orderItems}
            <!-- Kết thúc vòng lặp -->
            <tr class="total-row">
                <td colspan="3" style="text-align: right">Tổng tiền hàng:</td>
                <td>${subtotal}</td>
            </tr>
            <tr>
                <td colspan="3" style="text-align: right">Phí vận chuyển:</td>
                <td>${shippingFee}</td>
            </tr>
            <tr class="total-row">
                <td colspan="3" style="text-align: right">Tổng thanh toán:</td>
                <td>${totalAmount}</td>
            </tr>
            </tbody>
        </table>

        <div class="order-info">
            <h2>Thông tin giao hàng</h2>
            <p><strong>Người nhận:</strong> ${recipientName}</p>
            <p><strong>Địa chỉ:</strong> ${shippingAddress}</p>
            <p><strong>Số điện thoại:</strong> ${phoneNumber}</p>
            <p><strong>Dự kiến giao hàng:</strong> ${estimatedDelivery}</p>
        </div>

        <p>
            Bạn có thể theo dõi trạng thái đơn hàng của mình bằng cách nhấp vào
            nút bên dưới:
        </p>
        <div style="text-align: center">
            <a href="${trackingUrl}" class="button">Theo dõi đơn hàng</a>
        </div>

        <p>
            Nếu bạn có bất kỳ câu hỏi nào về đơn hàng, vui lòng liên hệ với chúng
            tôi qua email
            <a href="mailto:support@example.com">support@example.com</a> hoặc gọi
            số điện thoại <strong>1900 1234</strong>.
        </p>

        <p>Trân trọng,<br/>Đội ngũ hỗ trợ khách hàng</p>
    </div>
    <div class="footer">
        <p>&copy; 2025 Web-Ecommerce. Tất cả các quyền được bảo lưu.</p>
        <p>
            <a href="${websiteUrl}" style="color: #4caf50; text-decoration: none"
            >Trang chủ</a
            >
            |
            <a
                    href="${privacyPolicyUrl}"
                    style="color: #4caf50; text-decoration: none"
            >Chính sách bảo mật</a
            >
            |
            <a
                    href="${termsOfServiceUrl}"
                    style="color: #4caf50; text-decoration: none"
            >Điều khoản dịch vụ</a
            >
        </p>
    </div>
</div>
</body>
</html>