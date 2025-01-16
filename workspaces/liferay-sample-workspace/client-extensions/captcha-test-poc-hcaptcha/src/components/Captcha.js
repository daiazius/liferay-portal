import HCaptcha from '@hcaptcha/react-hcaptcha';

const onVerifyCaptcha = (token) => {
	console.log("Verified: " + token)
}

const Captcha = () => {
    return (
		<div>
			<HCaptcha sitekey="fa0cbf4c-adf4-4d96-98d0-442209e4658a" onVerify={onVerifyCaptcha}/>
		</div>
        
    );
}
 
export default Captcha;