<template>
	<body class="d-flex flex-column min-vh-100">
		<footer class="mt-auto">
			<div class="footer" id="footer">
				<div class="container">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
							<h4> Happy Learn </h4>
						</div>
						<div class="col-lg-3 col-sm-2 col-xs-3" style="margin-top: 5em">
							<h3> Autori </h3>
							<ul>
								<li> <p>Anna Fontana</p> </li>
								<li> <p>Enrico Chiesa</p> </li>
								<li> <p>Laurentiu Apostol</p> </li>
							</ul>
						</div>
					</div>
				</div>

				<div class="footer-bottom">
					<div class="container">
						<p class="pull-left copyright"> Copyright © Footer 2022. All right reserved. </p>
					</div>
				</div>
			</div>
		</footer>
	</body>
</template>

<script>
	export default {
		name: "Footer"
	}
</script>

<style scoped>
	.footer {
		background: #1B5613FF;
		height: auto;
		padding-bottom: 30px;
		position: relative;
		width: 100%;
		border-bottom: 1px solid #CCCCCC;
		border-top: 1px solid #DDDDDD;
	}
	.footer img {
		max-width: 100%;
	}
	.footer h3 {
		color: white;
		font-size: 18px;
		font-weight: 600;
		line-height: 27px;
		padding: 40px 0 0;
		text-transform: uppercase;
		margin-bottom: 15px;
	}
	.footer h4 {
		color: white;
		font-size: 2em;
		line-height: 38px;
		padding: 40px 0 10px;
		font-family: cursive;
		font-weight: lighter
	}
	.footer ul {
		font-size: 5px;
		list-style-type: none;
		margin-left: 0;
		padding-left: 0;
		margin-top: 0;
		color: #ffffff;
	}
	.footer p {
		margin: 0;
		color: white;
		font-weight: lighter;
		font-size: 15px;
	}
	.footer p:hover {
		text-decoration:none;
		font-weight: bold;
	}
	.footer-bottom {
		margin-top: 2em;
		border-top: 1px solid #DDDDDD;
		padding-top: 20px;
		padding-bottom: 10px;
	}
	.footer-bottom p.pull-left {
		padding-top: 6px;
		font-size: 0.75em
	}
</style>
