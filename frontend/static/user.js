function send_mail() {
	var body = document.getElementById("mail_body").value;
	send_notification(page_user_name, "Mail", body, function() {
		window.location.reload(true);
	});
}
